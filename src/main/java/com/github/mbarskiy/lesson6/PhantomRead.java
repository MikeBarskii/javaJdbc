package com.github.mbarskiy.lesson6;

import com.github.mbarskiy.ConnectionProperties;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

public class PhantomRead {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, InterruptedException {
        Properties properties = ConnectionProperties.getConnectionProperties();
        String url = properties.getProperty("url");

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(url, properties);
             Statement stat = conn.createStatement()) {

            conn.setAutoCommit(false);
//            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            ResultSet rs = stat.executeQuery("SELECT * FROM Books WHERE bookId > 5");
            while (rs.next()) {
                System.out.println(rs.getString("name") + " " + rs.getDouble(3));
            }

            new OtherTransaction().start();
            Thread.sleep(2000);

            ResultSet rs2 = stat.executeQuery("SELECT * FROM Books WHERE bookId > 5");
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
                conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                stat.executeUpdate("INSERT INTO Books (name, price) VALUES ('One More Book', 10)");
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
