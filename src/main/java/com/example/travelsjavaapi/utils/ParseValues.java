package com.example.travelsjavaapi.utils;

import com.example.travelsjavaapi.enumeration.TravelTypeEnum;
import com.example.travelsjavaapi.model.Travel;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ParseValues {

    private BigDecimal parseAmount(JsonNode travel) {
        return new BigDecimal((String) travel.get("amount").asText());
    }

    public LocalDateTime parseStartDate(JsonNode travel) {
        String startDate = (String) travel.get("startDate").asText();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        return ZonedDateTime.parse(startDate, formatter.withZone(ZoneId.of("UTC"))).toLocalDateTime();
    }

    public LocalDateTime parseEndDate(JsonNode travel) {
        String endDate = (String) travel.get("endDate").asText();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        return ZonedDateTime.parse(endDate, formatter.withZone(ZoneId.of("UTC"))).toLocalDateTime();
    }

    public void setTravelValues(JsonNode jsonTravel, Travel travel) {
        String type = jsonTravel.get("type") != null ? (String) jsonTravel.get("type").asText() : null;

        travel.setAmount(jsonTravel.get("amount") != null ? parseAmount(jsonTravel) : travel.getAmount());
        travel.setStartDate(jsonTravel.get("startDate") != null ? parseStartDate(jsonTravel) : travel.getStartDate());
        travel.setEndDate(jsonTravel.get("endDate") != null ? parseEndDate(jsonTravel) : travel.getEndDate());
        travel.setType(type != null ? TravelTypeEnum.getEnum(type) : travel.getType());
    }
}
