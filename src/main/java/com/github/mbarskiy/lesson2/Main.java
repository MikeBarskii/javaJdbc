package com.github.mbarskiy.lesson2;

import com.github.mbarskiy.ConnectionProperties;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        Properties properties = ConnectionProperties.getConnectionProperties();
        String url = properties.getProperty("url");

        Class.forName("com.mysql.cj.jdbc.Driver");

        String filename = "books.sql";
        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(filename)).getFile());

        try (Connection conn = DriverManager.getConnection(url, properties);
             Scanner scan = new Scanner(file);
             Statement statement = conn.createStatement()) {

            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                if (line.endsWith(";")) {
                    line = line.substring(0, line.length() - 1);
                }
                statement.executeUpdate(line);
            }
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM Books")) {
                while (resultSet.next()) {
                    int bookId = resultSet.getInt(1);
                    String bookName = resultSet.getString(2);
                    double bookPrice = resultSet.getDouble(3);
                    System.out.println("Id= " + bookId + " Name= " + bookName + " Price= " + bookPrice);
                }
            } catch (SQLException ex) {
                System.err.println("SQLException message: " + ex.getMessage());
                System.err.println("SQLException state: " + ex.getSQLState());
                System.err.println("SQLException error code: " + ex.getErrorCode());
            }
        }
    }
}
