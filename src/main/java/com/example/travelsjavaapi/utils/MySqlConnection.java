package com.example.travelsjavaapi.utils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import io.github.cdimascio.dotenv.Dotenv;

public class MySqlConnection {

    private static Dotenv dotenv = Dotenv.load();
    private static String dbUrl = dotenv.get("DB_URL");
    private static String dbUsername = dotenv.get("DB_USERNAME");
    private static String dbPassword = dotenv.get("DB_PASSWORD");

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
