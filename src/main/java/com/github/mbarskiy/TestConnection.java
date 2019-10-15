package com.github.mbarskiy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306/first_lesson";
        String username = "root";
        String password = "1234";

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connection successfully initialized");
        }
    }
}
