package com.library.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    public static Connection mainconnection;


    public void setMainconnection() {

//        mainconnection = connection;
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/?";
        String user = "root"; //
        String password = "----";

        Connection connection = null;

        try {
            // Load JDBC driver (make sure mysql-connector-j.jar is in your classpath)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            connection = DriverManager.getConnection(url, user, password);

//            if (connection != null) {
//                System.out.println("✅ Connection established successfully!");
//            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found. Please add the jar file to classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        } finally {
//            try {
//                if (connection != null) {
//                    connection.close();
//                    System.out.println("🔒 Connection closed.");
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
        }
        return connection;
    }
}
