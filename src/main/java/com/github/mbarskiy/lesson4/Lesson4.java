package com.github.mbarskiy.lesson4;

import com.github.mbarskiy.ConnectionProperties;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Lesson4 {

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        Properties properties = ConnectionProperties.getConnectionProperties();
        String url = properties.getProperty("url");

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(url, properties);
             Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            try (ResultSet rs = statement.executeQuery("SELECT * FROM Books")) {
//                while (rs.next()) {
//                    int id = rs.getInt(1);
//                    double price = rs.getDouble(3);
//                    if (id == 4) {
//                        rs.updateString("name", "Spartacus (discount)");
//                        rs.updateDouble(3, price - 10);
//                        rs.updateRow();
//                    }
//                }
                if (rs.absolute(2))
                    System.out.println(rs.getString("name"));
                if (rs.previous())
                    System.out.println(rs.getString("name"));
                if (rs.last())
                    System.out.println(rs.getString("name"));
                if (rs.relative(-3)) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    while (rs.next()) {
                        for (int i = 1; i < rsmd.getColumnCount(); i++) {
                            String field = rsmd.getColumnName(i);
                            String value = rs.getString(field);
                            System.out.print(field + ": " + value + " ");
                        }
                        System.out.println();
                    }
                }
            }
        }
    }

}
