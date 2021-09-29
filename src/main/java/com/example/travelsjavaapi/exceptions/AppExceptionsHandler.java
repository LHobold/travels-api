package com.example.travelsjavaapi.exceptions;

import java.sql.SQLException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class AppExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { SQLException.class })
    public ResponseEntity<Object> handleSqlException(SQLException ex) {
        return this.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return this.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(value = { InvalidFormatException.class })
    public ResponseEntity<Object> handleInvalidFormatException(Exception ex) {
        return this.buildResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(value = { CommunicationsException.class })
    public ResponseEntity<Object> handleCommunicatioException(Exception ex) {
        return this.buildResponse(HttpStatus.NOT_FOUND, ex, "Database is down, please try again later");
    }

    @ExceptionHandler(value = { NotFoundException.class })
    public ResponseEntity<Object> handleNotFoundException(Exception ex) {
        return this.buildResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(value = { WrongDateException.class })
    public ResponseEntity<Object> handleWrongDateException(Exception ex) {
        return this.buildResponse(HttpStatus.BAD_REQUEST, ex);
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, Exception ex) {
        System.out.println("Exception: " + ex.getMessage());
        ApiError errorMessage = new ApiError(status, ex.getLocalizedMessage(), ex.getClass().getSimpleName());
        return new ResponseEntity<Object>(errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, Exception ex, String message) {
        System.out.println("Exception: " + ex.getMessage());
        ApiError errorMessage = new ApiError(status, message, ex.getClass().getSimpleName());
        return new ResponseEntity<Object>(errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }
}
