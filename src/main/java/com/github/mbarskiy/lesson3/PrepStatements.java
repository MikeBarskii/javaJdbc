package com.github.mbarskiy.lesson3;

import com.github.mbarskiy.ConnectionProperties;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class PrepStatements {

    public static void main(String[] args) throws IOException, SQLException {
        Properties properties = ConnectionProperties.getConnectionProperties();
        String url = properties.getProperty("url");

        try (Connection conn = DriverManager.getConnection(url, properties);
             PreparedStatement preparedStat =
                     conn.prepareStatement("INSERT INTO Books (name, price) VALUES (?, ?)")) {

            preparedStat.setString(1, "Schindler's list");
            preparedStat.setDouble(2, 32.5);
            preparedStat.execute();

            try (ResultSet rs = preparedStat.executeQuery("SELECT * FROM Books")) {
                while (rs.next()) {
                    int bookId = rs.getInt(1);
                    String bookName = rs.getString(2);
                    double bookPrice = rs.getDouble(3);
                    System.out.println("Id= " + bookId + " Name= " + bookName + " Price= " + bookPrice);
                }
            }
        }
    }
}
