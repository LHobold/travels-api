package com.example.travelsjavaapi.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.travelsjavaapi.enumeration.TravelTypeEnum;
import com.example.travelsjavaapi.model.Travel;
import com.example.travelsjavaapi.utils.MySqlConnection;
import com.example.travelsjavaapi.utils.ParseValues;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.stereotype.Component;

@Component
public class TravelDao {

    private ParseValues parseValues;

    public Travel saveTravel(Travel travel) throws Exception {
        try (Connection conn = MySqlConnection.getConnection();) {

            Timestamp sqlStartDate = Timestamp.valueOf(travel.getStartDate());
            Timestamp sqlEndDate = Timestamp.valueOf(travel.getEndDate());
            String query = " INSERT into Travels (amount, type, startDate, endDate)" + " values (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {

                stmt.setBigDecimal(1, travel.getAmount());
                stmt.setString(2, travel.getType().name());
                stmt.setTimestamp(3, sqlStartDate);
                stmt.setTimestamp(4, sqlEndDate);
                stmt.execute();

                ResultSet generatedKey = stmt.getGeneratedKeys();

                generatedKey.next();
                travel.setId(generatedKey.getLong(1));
                return travel;
            }
        }
    }

    public Travel findById(long id) throws Exception {
        try (Connection conn = MySqlConnection.getConnection(); Statement stmt = conn.createStatement()) {
            String queryId = String.valueOf(id);
            String query = String.format("SELECT * FROM Travels where ID = %s", queryId);

            try (ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    Travel foundTravel = new Travel();
                    foundTravel.setId(Long.valueOf(rs.getInt("id")));
                    foundTravel.setAmount(rs.getBigDecimal("amount"));
                    foundTravel.setType(TravelTypeEnum.valueOf(rs.getString("Type")));
                    foundTravel.setStartDate(rs.getTimestamp("startDate").toLocalDateTime());
                    foundTravel.setEndDate(rs.getTimestamp("endDate").toLocalDateTime());
                    return foundTravel;
                }

                return null;
            }
        }
    }

    public void deleteOne(long id) throws Exception {
        try (Connection conn = MySqlConnection.getConnection(); Statement stmt = conn.createStatement()) {
            String queryId = String.valueOf(id);
            String query = String.format("DELETE FROM Travels where ID = %s", queryId);
            stmt.executeUpdate(query);
        }
    }

    public void deleteAll() throws Exception {
        try (Connection conn = MySqlConnection.getConnection(); Statement stmt = conn.createStatement()) {
            String query = "TRUNCATE TABLE Travels";
            stmt.executeUpdate(query);
        }
    }

    public List<Travel> findAll() throws Exception {
        try (Connection conn = MySqlConnection.getConnection(); Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM Travels.Travels";
            try (ResultSet rs = stmt.executeQuery(query)) {
                List<Travel> travels = new ArrayList<>();

                while (rs.next()) {
                    Travel foundTravel = new Travel();
                    foundTravel.setId(Long.valueOf(rs.getInt("id")));
                    foundTravel.setAmount(rs.getBigDecimal("amount"));
                    foundTravel.setType(TravelTypeEnum.valueOf(rs.getString("Type")));
                    foundTravel.setStartDate(rs.getTimestamp("startDate").toLocalDateTime());
                    foundTravel.setEndDate(rs.getTimestamp("endDate").toLocalDateTime());
                    travels.add(foundTravel);
                }

                return travels;
            }
        }
    }

    public Travel updateOne(Travel travelToUpdate, JsonNode fieldsToUpdate) throws Exception {
        try (Connection conn = MySqlConnection.getConnection();) {
            String queryId = String.valueOf(travelToUpdate.getId());
            String query = String.format("UPDATE Travels SET amount=?, type=?, startDate=?, endDate=? WHERE ID = %s",
                    queryId);

            BigDecimal sqlAmount = fieldsToUpdate.get("amount") != null
                    ? new BigDecimal((String) fieldsToUpdate.get("amount").asText())
                    : travelToUpdate.getAmount();

            Timestamp sqlStartDate = fieldsToUpdate.get("startDate") != null
                    ? Timestamp.valueOf(parseValues.parseStartDate(fieldsToUpdate))
                    : Timestamp.valueOf(travelToUpdate.getStartDate());

            Timestamp sqlEndDate = fieldsToUpdate.get("endDate") != null
                    ? Timestamp.valueOf(parseValues.parseEndDate(fieldsToUpdate))
                    : Timestamp.valueOf(travelToUpdate.getEndDate());

            TravelTypeEnum sqlType = fieldsToUpdate.get("type") != null
                    ? TravelTypeEnum.getEnum(fieldsToUpdate.get("type").asText())
                    : travelToUpdate.getType();

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setBigDecimal(1, sqlAmount);
                stmt.setString(2, sqlType.name());
                stmt.setTimestamp(3, sqlStartDate);
                stmt.setTimestamp(4, sqlEndDate);
                stmt.executeUpdate();

                Travel updatedTravel = travelToUpdate;
                parseValues.setTravelValues(fieldsToUpdate, updatedTravel);

                return updatedTravel;
            }
        }
    }

}
