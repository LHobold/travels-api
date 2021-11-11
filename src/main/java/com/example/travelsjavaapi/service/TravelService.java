package com.example.travelsjavaapi.service;

import com.example.travelsjavaapi.dao.TravelDao;
import com.example.travelsjavaapi.exceptions.InvalidAmountException;
import com.example.travelsjavaapi.exceptions.NotFoundException;
import com.example.travelsjavaapi.exceptions.WrongDateException;

import com.example.travelsjavaapi.model.Travel;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TravelService {

    @Autowired
    private TravelDao travelDao;

    private ObjectMapper objectMapper = new ObjectMapper();

    private boolean isStartDateGreaterThanEnd(Travel travel) {
        if (travel.getEndDate() == null) {
            return false;
        }
        return travel.getStartDate().isAfter(travel.getEndDate());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Travel createTravel(String jsonString) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        Travel travel = objectMapper.readValue(jsonString, Travel.class);

        if (isStartDateGreaterThanEnd(travel)) {
            throw new WrongDateException("The provided start date is greater than the end date!");
        }

        if ((travel.getAmount().intValue()) < 0) {
            throw new InvalidAmountException("The amount should be more than 0!");
        }

        Travel createdTravel = travelDao.saveTravel(travel);
        return createdTravel;
    }

    public Travel updateTravel(String jsonFieldsToUpdate, long id) throws Exception {
        Travel travelToUpdate = travelDao.findById(id);

        if (travelToUpdate == null) {
            throw new NotFoundException("Could not find an travel with id: " + id);
        }

        objectMapper.registerModule(new JavaTimeModule());
        JsonNode fieldsToUpdate = objectMapper.readTree(jsonFieldsToUpdate);

        return travelDao.updateOne(travelToUpdate, fieldsToUpdate);
    }

    public void deleteTravel(long id) throws Exception {
        travelDao.deleteOne(id);
    }

    public List<Travel> findAllTravels() throws Exception {
        return travelDao.findAll();
    }

    public Travel findTravelById(long id) throws Exception {
        Travel foundTravel = travelDao.findById(id);

        if (foundTravel == null) {
            throw new NotFoundException("Could not find an travel with id: " + id);
        }

        return foundTravel;
    }

    public void deleteAllTravels() throws Exception {
        travelDao.deleteAll();
    }
}