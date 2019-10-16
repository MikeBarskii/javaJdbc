package com.github.mbarskiy.lesson6;

import com.github.mbarskiy.ConnectionProperties;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

public class NonRepeatableRead {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, InterruptedException {
        Properties properties = ConnectionProperties.getConnectionProperties();
        String url = properties.getProperty("url");

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(url, properties);
             Statement stat = conn.createStatement()) {

            conn.setAutoCommit(false);
//            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            ResultSet rs = stat.executeQuery("SELECT * FROM Books");
            while (rs.next()) {
                System.out.println(rs.getString("name") + " " + rs.getDouble(3));
            }

            new OtherTransaction().start();
            Thread.sleep(2000);

            ResultSet rs2 = stat.executeQuery("SELECT * FROM Books");
            while (rs2.next()) {
                System.out.println(rs2.getString("name") + " " + rs2.getDouble(3));
            }
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
//                conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                stat.executeUpdate("UPDATE Books SET price = price + 20 WHERE name='Green Mile'");
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
