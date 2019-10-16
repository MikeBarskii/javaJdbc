package com.github.mbarskiy.lesson6;

import com.github.mbarskiy.ConnectionProperties;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

public class DirtyRead {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, InterruptedException {
        Properties properties = ConnectionProperties.getConnectionProperties();
        String url = properties.getProperty("url");

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(url, properties);
             Statement stat = conn.createStatement()) {

            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

            stat.executeUpdate("UPDATE Books SET price = 100 WHERE bookId=1");
            new OtherTransaction().start();
            Thread.sleep(2000);
            conn.rollback();
        }
    }

    private static class OtherTransaction extends Thread {
        @Override
        public void run() {
            Properties properties = null;
            try {
                properties = ConnectionProperties.getConnectionProperties();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String url = Objects.requireNonNull(properties).getProperty("url");

            try (Connection conn = DriverManager.getConnection(url, properties);
                 Statement stat = conn.createStatement()) {

                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

                ResultSet rs = stat.executeQuery("SELECT * FROM Books");
                while (rs.next()) {
                    System.out.println(rs.getString("name") + " " + rs.getDouble(3));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
