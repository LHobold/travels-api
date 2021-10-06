package com.example.travelsjavaapi.utils;

import java.sql.Connection;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MyListener {

    @EventListener(ApplicationReadyEvent.class)
    public void createMySqlTablesOnStart() throws Exception {

        try (Connection conn = MySqlConnection.getConnection()) {
            MySqlConnection.createTravelsTable(conn);
            MySqlConnection.createUsersTable(conn);
            System.out.println("Sql tables created");
        } catch (Exception ex) {
            System.out.println("An exception occoured while trying to create MySqlTables: " + ex);
        }
    }

}
