package com.example.travelsjavaapi.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.HttpStatus;

import com.example.travelsjavaapi.service.TravelService;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.example.travelsjavaapi.model.Travel;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "/api/v1/travels", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
                MediaType.APPLICATION_JSON_VALUE })
public class TravelController {

        private static final Logger LOGGER = LoggerFactory.getLogger(TravelController.class);

        @Autowired
        private TravelService travelService;

        @GetMapping
        public ResponseEntity<List<Travel>> find() {
                try {
                        if (travelService.findAllTravels().isEmpty()) {
                                return ResponseEntity.notFound().build();
                        }
                        // LOGGER.info(travelService.findAllTravels().toString());
                        return ResponseEntity.ok(travelService.findAllTravels());

                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }

        }

        @GetMapping(path = "/{id}")
        public ResponseEntity<Travel> findOne(@PathVariable("id") long id) {
                try {
                        Travel foundTravel = travelService.findTravelById(id);
                        if (foundTravel == null) {
                                return ResponseEntity.notFound().build();
                        }
                        return ResponseEntity.ok(foundTravel);
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

                }
        }

        @PostMapping
        public ResponseEntity<Travel> createTravel(@RequestBody String travel) {
                try {
                        Travel createdTravel = travelService.createTravel(travel);
                        if (createdTravel == null) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                        }
                        System.out.println(createdTravel);
                        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                                        .path((String.valueOf(createdTravel.getId()))).build().toUri();

                        return ResponseEntity.created(uri).body(createdTravel);
                } catch (SQLException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                } catch (UnrecognizedPropertyException e) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
        }

        @DeleteMapping
        public ResponseEntity<Boolean> deleteAllTravels() {
                try {
                        travelService.deleteAllTravels();
                        return ResponseEntity.noContent().build();
                } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

                }
        }

        @DeleteMapping(path = "/{id}")
        public ResponseEntity<Boolean> deleteOneTravel(@PathVariable("id") long id) {

                try {
                        Travel foundTravel = travelService.findTravelById(id);
                        if (foundTravel == null) {
                                return ResponseEntity.notFound().build();
                        }
                        travelService.deleteTravel(id);
                        return ResponseEntity.noContent().build();

                } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                }

        }

        @PutMapping(path = "/{id}")
        @ResponseBody
        public ResponseEntity<Travel> updateTravel(@PathVariable("id") long id, @RequestBody String updateFields) {
                try {
                        Travel travelToUpdate = travelService.findTravelById(id);
                        if (travelToUpdate == null) {
                                LOGGER.error("Travel not found.");
                                return ResponseEntity.notFound().build();
                        }

                        Travel updatedTravel = travelService.updateTravel(updateFields, travelToUpdate);
                        return ResponseEntity.ok(updatedTravel);

                } catch (Exception e) {
                        LOGGER.error("JSON fields are not parsable. " + e);
                        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
                }

        }

}
