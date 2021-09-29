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

        ApiError errorMessage = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(),
                ex.getClass().getSimpleName());

        return new ResponseEntity<Object>(errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = { Exception.class, RuntimeException.class })
    public ResponseEntity<Object> handleSqlException(Exception ex) {

        ApiError errorMessage = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(),
                ex.getClass().getSimpleName());

        return new ResponseEntity<Object>(errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = { InvalidFormatException.class })
    public ResponseEntity<Object> handleInvalidFormatException(Exception ex) {

        ApiError errorMessage = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(),
                ex.getClass().getSimpleName());

        return new ResponseEntity<Object>(errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = { CommunicationsException.class })
    public ResponseEntity<Object> handleCommunicatioException(Exception ex) {

        ApiError errorMessage = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Database is down, please try again later", ex.getClass().getSimpleName());

        return new ResponseEntity<Object>(errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = { NotFoundException.class })
    public ResponseEntity<Object> handleNotFoundException(Exception ex) {

        ApiError errorMessage = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(),
                ex.getClass().getSimpleName());

        return new ResponseEntity<Object>(errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = { WrongDateException.class })
    public ResponseEntity<Object> handleWrongDateException(Exception ex) {

        ApiError errorMessage = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(),
                ex.getClass().getSimpleName());

        return new ResponseEntity<Object>(errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

}
