package org.example.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    public static Connection getConnection() throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream("database.properties");

        if (inputStream == null) {
            throw new RuntimeException("Khong tim thay file database.properties");
        }

        properties.load(inputStream);

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        return DriverManager.getConnection(url, user, password);
    }
}
