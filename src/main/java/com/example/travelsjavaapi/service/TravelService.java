package com.example.travelsjavaapi.service;

import com.example.travelsjavaapi.enumeration.TravelTypeEnum;
import com.example.travelsjavaapi.exceptions.WrongDateException;
import com.example.travelsjavaapi.utils.MySqlConnection;
import com.example.travelsjavaapi.model.Travel;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

@Service
public class TravelService {

    private ObjectMapper objectMapper = new ObjectMapper();

    private BigDecimal parseAmount(JsonNode travel) {
        return new BigDecimal((String) travel.get("amount").asText());
    }

    private LocalDateTime parseStartDate(JsonNode travel) {
        String startDate = (String) travel.get("startDate").asText();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        return ZonedDateTime.parse(startDate, formatter.withZone(ZoneId.of("UTC"))).toLocalDateTime();
    }

    private LocalDateTime parseEndDate(JsonNode travel) {
        String endDate = (String) travel.get("endDate").asText();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        return ZonedDateTime.parse(endDate, formatter.withZone(ZoneId.of("UTC"))).toLocalDateTime();
    }

    private boolean isStartDateGreaterThanEnd(Travel travel) {
        if (travel.getEndDate() == null) {
            return false;
        }
        return travel.getStartDate().isAfter(travel.getEndDate());
    }

    private void setTravelValues(JsonNode jsonTravel, Travel travel) {
        String type = jsonTravel.get("type") != null ? (String) jsonTravel.get("type").asText() : null;

        travel.setAmount(jsonTravel.get("amount") != null ? parseAmount(jsonTravel) : travel.getAmount());
        travel.setStartDate(jsonTravel.get("startDate") != null ? parseStartDate(jsonTravel) : travel.getStartDate());
        travel.setEndDate(jsonTravel.get("endDate") != null ? parseEndDate(jsonTravel) : travel.getEndDate());
        travel.setType(type != null ? TravelTypeEnum.getEnum(type) : travel.getType());
    }

    public Travel createTravel(String jsonString) throws Exception {

        try (Connection conn = MySqlConnection.getConnection();) {
            objectMapper.registerModule(new JavaTimeModule());
            Travel travel = objectMapper.readValue(jsonString, Travel.class);

            if (isStartDateGreaterThanEnd(travel)) {
                throw new WrongDateException("The provided start date is greater than the end date!");
            }

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

                if (generatedKey.next()) {
                    travel.setId(generatedKey.getLong(1));
                    return travel;
                }

                return null;
            }
        }
    }

    public Travel updateTravel(String jsonString, Travel travel) throws Exception {
        try (Connection conn = MySqlConnection.getConnection();) {
            objectMapper.registerModule(new JavaTimeModule());
            Travel travelToUpdate = travel;
            JsonNode fieldsToUpdate = objectMapper.readTree(jsonString);

            String queryId = String.valueOf(travelToUpdate.getId());
            String query = String.format("UPDATE Travels SET amount=?, type=?, startDate=?, endDate=? WHERE ID = %s",
                    queryId);

            BigDecimal sqlAmount = fieldsToUpdate.get("amount") != null
                    ? new BigDecimal((String) fieldsToUpdate.get("amount").asText())
                    : travelToUpdate.getAmount();

            Timestamp sqlStartDate = fieldsToUpdate.get("startDate") != null
                    ? Timestamp.valueOf(parseStartDate(fieldsToUpdate))
                    : Timestamp.valueOf(travelToUpdate.getStartDate());

            Timestamp sqlEndDate = fieldsToUpdate.get("endDate") != null
                    ? Timestamp.valueOf(parseEndDate(fieldsToUpdate))
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
                setTravelValues(fieldsToUpdate, updatedTravel);

                return updatedTravel;
            }
        }
    }

    public void deleteTravel(long id) throws Exception {
        try (Connection conn = MySqlConnection.getConnection(); Statement stmt = conn.createStatement()) {
            String queryId = String.valueOf(id);
            String query = String.format("DELETE FROM Travels where ID = %s", queryId);
            stmt.executeUpdate(query);
        }
    }

    public List<Travel> findAllTravels() throws Exception {
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

    public Travel findTravelById(long id) throws Exception {
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

    public void deleteAllTravels() throws Exception {
        try (Connection conn = MySqlConnection.getConnection(); Statement stmt = conn.createStatement()) {
            String query = "TRUNCATE TABLE Travels";
            stmt.executeUpdate(query);
        }
    }
}
