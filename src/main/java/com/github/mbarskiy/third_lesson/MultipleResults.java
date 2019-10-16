package com.github.mbarskiy.third_lesson;

import com.github.mbarskiy.ConnectionProperties;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class MultipleResults {
    public static void main(String[] args) throws IOException, SQLException {
        Properties properties = ConnectionProperties.getConnectionProperties();
        String url = properties.getProperty("url");

        try (Connection conn = DriverManager.getConnection(url, properties);
             CallableStatement callStat = conn.prepareCall("{CALL tablesCount}")) {
            boolean hasResults = callStat.execute();

            ResultSet rs = null;
            try {
                while (hasResults) {
                    rs = callStat.getResultSet();
                    while (rs.next()) {
                        System.out.println("Number of rows in the table: " + rs.getInt(1));
                    }
                    hasResults = callStat.getMoreResults();
                }
            } finally {
                if (rs != null) {
                    rs.close();
                }
            }
        }
    }
}
