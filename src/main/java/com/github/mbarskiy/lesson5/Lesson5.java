package com.github.mbarskiy.lesson5;

import com.github.mbarskiy.ConnectionProperties;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Lesson5 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        Properties properties = ConnectionProperties.getConnectionProperties();
        String url = properties.getProperty("url");

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(url, properties);
             Statement stat = conn.createStatement()) {
            String createTable = "CREATE TABLE Fruit (name VARCHAR(15) NOT NULL, amount INTEGER, price DOUBLE NOT NULL, PRIMARY KEY (name))";
            String command1 = "INSERT INTO Fruit (name, amount, price) VALUES ('Apple', 200, 3.50)";
            String command2 = "INSERT INTO Fruit (name, amount, price) VALUES ('Orange', 50, 5.50)";
            String command3 = "INSERT INTO Fruit (name, amount, price) VALUES ('Lemons', 30, 3.0)";
            String command4 = "INSERT INTO Fruit (name, amount, price) VALUES ('Pineapple', 10, 8.0)";

//            conn.setAutoCommit(false);
//            stat.executeUpdate(createTable);
//            stat.executeUpdate(command1);
//            Savepoint spt = conn.setSavepoint();
//            stat.executeUpdate(command2);
//            stat.executeUpdate(command3);
//            stat.executeUpdate(command4);
//            conn.commit();
//            conn.rollback(spt);
//            conn.commit();
//            conn.releaseSavepoint(spt);
            conn.setAutoCommit(true);
            stat.addBatch(createTable);
            stat.addBatch(command1);
            stat.addBatch(command2);
            stat.addBatch(command3);
            stat.addBatch(command4);
            stat.executeBatch();
        }
    }
}
