package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/restaurant_db";
        String user = "root";
        String pass = "dinhne123";
        return DriverManager.getConnection(url, user, pass);
    }
}
