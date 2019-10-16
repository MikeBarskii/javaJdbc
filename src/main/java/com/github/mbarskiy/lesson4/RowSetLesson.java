package com.github.mbarskiy.lesson4;

import com.github.mbarskiy.ConnectionProperties;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class RowSetLesson {

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        ResultSet resultSet = getResultSet();
//        while (resultSet.next())
//            System.out.println(resultSet.getString("name"));

        CachedRowSet rowSet = (CachedRowSet) resultSet;
        rowSet.setCommand("SELECT * FROM Books WHERE price > ?");
        rowSet.setDouble(1, 30);

        Properties properties = ConnectionProperties.getConnectionProperties();
        rowSet.setUrl(properties.getProperty("url"));
        rowSet.setUsername(properties.getProperty("user"));
        rowSet.setPassword(properties.getProperty("password"));
        rowSet.execute();

        while (rowSet.next()) {
            String name = rowSet.getString("name");
            double price = rowSet.getDouble(3);
            System.out.println(name + " " + price);
        }
    }

    private static ResultSet getResultSet() throws IOException, ClassNotFoundException, SQLException {
        Properties properties = ConnectionProperties.getConnectionProperties();
        String url = properties.getProperty("url");

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(url, properties);
             Statement stat = conn.createStatement()) {
            ResultSet resultSet = stat.executeQuery("SELECT * FROM Books");

            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet cachedRowSet = factory.createCachedRowSet();
            cachedRowSet.populate(resultSet);
            return cachedRowSet;
        }
    }
}
