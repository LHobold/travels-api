package com.example.travelsjavaapi.utils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MySqlConnection {

    private static String dbUrl;
    private static String dbUsername;
    private static String dbPassword;

    @Value("${DATABASE_URL:jdbc:mysql://localhost:3306/Travels}")
    public void setDatabaseUrl(String db) {
        dbUrl = db;
    }

    @Value("${DATABASE_USERNAME:admin}")
    public void setDatabaseUser(String db) {
        dbUsername = db;
    }

    @Value("${DATABASE_PASSWORD:admin}")
    public void setDatabasePassword(String db) {
        dbPassword = db;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }

    public static void createTravelsTable(Connection conn) throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS `Travels`.`Travels` (" + "`id` INT NOT NULL AUTO_INCREMENT,"
                + "`startDate` DATETIME NOT NULL," + "`endDate` DATETIME NOT NULL," + "`amount` DECIMAL(20) NOT NULL,"
                + "`type` VARCHAR(45) NOT NULL," + " PRIMARY KEY (`id`));";

        Statement stmt = conn.createStatement();
        stmt.execute(query);
    }

    public static void createUsersTable(Connection conn) throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS `Travels`.`Userss` (" + "`id` INT NOT NULL AUTO_INCREMENT,"
                + "`username` VARCHAR(45) NOT NULL," + "`password` VARCHAR(255) NOT NULL," + " PRIMARY KEY (`id`));";

        Statement stmt = conn.createStatement();
        stmt.execute(query);
    }

}
