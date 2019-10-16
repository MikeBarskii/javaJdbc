package com.github.mbarskiy.lesson3;

import com.github.mbarskiy.ConnectionProperties;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class CallStatements {

    public static void main(String[] args) throws IOException, SQLException {
        Properties properties = ConnectionProperties.getConnectionProperties();
        String url = properties.getProperty("url");

        try (Connection conn = DriverManager.getConnection(url, properties);
             CallableStatement callStat = conn.prepareCall("{CALL booksCount(?)}")) {

            callStat.registerOutParameter(1, Types.INTEGER);
            callStat.execute();
            System.out.println("Number of rows in the table: " + callStat.getInt(1));
        }
    }
}
