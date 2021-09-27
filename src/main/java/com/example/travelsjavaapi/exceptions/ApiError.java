package com.example.travelsjavaapi.exceptions;

import lombok.Data;
import lombok.Getter;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.List;

@Data
@Getter
public class ApiError {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        this.errors = Arrays.asList(error);
    }
}