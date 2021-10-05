package com.example.travelsjavaapi.exceptions;

import lombok.Data;
import lombok.Getter;

import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@Getter
public class ApiError {

    private HttpStatus status;
    private String timestamp;
    private String message;
    private List<String> errors;

    private String currentTime() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(date);

    }

    public ApiError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = currentTime();
    }

    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        this.errors = Arrays.asList(error);
        this.timestamp = currentTime();
    }
}