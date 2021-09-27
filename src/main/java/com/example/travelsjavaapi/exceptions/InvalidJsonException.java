package com.example.travelsjavaapi.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidJsonException extends Exception {
    public InvalidJsonException(String message) {
        super(message);

    }
}