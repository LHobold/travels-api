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

import com.example.travelsjavaapi.service.TravelService;
import com.example.travelsjavaapi.model.Travel;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/travels", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
                MediaType.APPLICATION_JSON_VALUE })
public class TravelController {

        @Autowired
        private TravelService travelService;

        @GetMapping
        public ResponseEntity<List<Travel>> find() throws Exception {
                List<Travel> travels = travelService.findAllTravels();
                return ResponseEntity.ok(travels);
        }

        @GetMapping(path = "/{id}")
        public ResponseEntity<Travel> findOne(@PathVariable("id") long id) throws Exception {
                Travel foundTravel = travelService.findTravelById(id);
                return ResponseEntity.ok(foundTravel);
        }

        @PostMapping
        public ResponseEntity<Travel> createTravel(@RequestBody String travel) throws Exception {
                Travel createdTravel = travelService.createTravel(travel);
                var uri = ServletUriComponentsBuilder.fromCurrentRequest().path((String.valueOf(createdTravel.getId())))
                                .build().toUri();

                return ResponseEntity.created(uri).body(createdTravel);
        }

        @DeleteMapping
        public ResponseEntity<Boolean> deleteAllTravels() throws Exception {
                travelService.deleteAllTravels();
                return ResponseEntity.noContent().build();
        }

        @DeleteMapping(path = "/{id}")
        public ResponseEntity<Boolean> deleteOneTravel(@PathVariable("id") long id) throws Exception {
                travelService.deleteTravel(id);
                return ResponseEntity.noContent().build();
        }

        @PutMapping(path = "/{id}")
        @ResponseBody
        public ResponseEntity<Travel> updateTravel(@PathVariable("id") long id, @RequestBody String updateFields)
                        throws Exception {
                Travel updatedTravel = travelService.updateTravel(updateFields, id);
                return ResponseEntity.ok(updatedTravel);
        }
}
