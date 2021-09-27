package com.example.travelsjavaapi.controller;

import com.example.travelsjavaapi.model.Statistic;
import com.example.travelsjavaapi.model.Travel;
import com.example.travelsjavaapi.service.StatisticsService;
import com.example.travelsjavaapi.service.TravelService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    @Autowired
    private TravelService travelService;

    @Autowired
    private StatisticsService statsService;

    @RequestMapping(value = "/api/v1/stats", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Statistic> showStats() {
        try {
            List<Travel> travels = travelService.findAllTravels();
            Statistic stats = statsService.create(travels);
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
