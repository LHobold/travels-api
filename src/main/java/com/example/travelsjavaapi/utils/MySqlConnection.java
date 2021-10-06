package com.example.travelsjavaapi.utils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.lang.Strings;

public class MySqlConnection {

    private static Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static String dbUrl = Strings.hasText(dotenv.get("DB_URL")) ? dotenv.get("DB_URL")
            : "jdbc:mysql://localhost:3306/Travels";
    private static String dbUsername = Strings.hasText(dotenv.get("DB_USERNAME")) ? dotenv.get("DB_USERNAME") : "admin";
    private static String dbPassword = Strings.hasText(dotenv.get("DB_PASSWORD")) ? dotenv.get("DB_USERNAME") : "admin";

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
