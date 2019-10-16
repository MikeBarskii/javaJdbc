package com.github.mbarskiy.first_lesson;

import com.github.mbarskiy.ConnectionProperties;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestConnection {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        Properties properties = ConnectionProperties.getConnectionProperties();
        String url = properties.getProperty("url");

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(url, properties)) {
            System.out.println("Connection successfully initialized");
        }
    }
}
