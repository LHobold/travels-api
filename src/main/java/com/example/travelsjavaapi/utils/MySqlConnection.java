package com.example.travelsjavaapi.utils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class MySqlConnection {
    private static String dbUrl = "jdbc:mysql://localhost:3306/Travels";
    private static String dbUsername = "admin";
    private static String dbPassword = "admin";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }

}
