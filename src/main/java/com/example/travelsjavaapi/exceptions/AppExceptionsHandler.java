package com.example.travelsjavaapi.exceptions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.AuthenticationException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class AppExceptionsHandler extends ResponseEntityExceptionHandler {

    // 500 EXCEPTIONS
    ////////////////////////////

    @ExceptionHandler(value = { Exception.class, SQLException.class })
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return this.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(value = { CommunicationsException.class })
    public ResponseEntity<Object> handleCommunicatioException(Exception ex) {
        return this.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, "Database is down, please try again later");
    }

    // 404 EXCEPTIONS

    @ExceptionHandler(value = { NotFoundException.class })
    public ResponseEntity<Object> handleNotFoundException(Exception ex) {
        return this.buildResponse(HttpStatus.NOT_FOUND, ex);
    }

    // JSON FORMAT EXCEPTIONS
    /////////////////////////

    @ExceptionHandler(value = { InvalidFormatException.class })
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex) {

        String message = "Could not deserialize " + "|" + ex.getValue() + "|" + " from "
                + ex.getValue().getClass().getSimpleName() + " into " + ex.getTargetType().getSimpleName() + " at key: "
                + ex.getPath().get(0).getFieldName();

        if (ex.getTargetType().isEnum()) {
            List<String> enumValues = new ArrayList<>();
            for (Object obj : ex.getTargetType().getEnumConstants()) {
                enumValues.add(obj.toString());
            }

            message = "|" + ex.getValue() + "|" + " is not a type of the following travel types: " + enumValues;
        }

        return this.buildResponse(HttpStatus.BAD_REQUEST, ex, message);
    }

    @ExceptionHandler(value = { JsonEOFException.class, JsonParseException.class })
    public ResponseEntity<Object> handleJsonEOFException(Exception ex) {
        return this.buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex, "JSON format is wrong");
    }

    @ExceptionHandler(value = { UnrecognizedPropertyException.class })
    public ResponseEntity<Object> handleUnrecognizedPropertyException(Exception ex) {
        return this.buildResponse(HttpStatus.BAD_REQUEST, ex, "Wrong JSON properties");
    }

    @ExceptionHandler(value = { WrongDateException.class })
    public ResponseEntity<Object> handleWrongDateException(Exception ex) {
        return this.buildResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(value = { InvalidAmountException.class })
    public ResponseEntity<Object> handleInvalidAmountException(Exception ex) {
        return this.buildResponse(HttpStatus.BAD_REQUEST, ex);
    }

    // 401 EXCEPTIONS
    ///////////////////

    @ExceptionHandler(value = { UsernameNotFoundException.class, AuthFailedException.class })
    public ResponseEntity<Object> handleUsernameNotFoundException(Exception ex) {
        return this.buildResponse(HttpStatus.UNAUTHORIZED, ex, "Wrong username or password");
    }

    @ExceptionHandler(value = { ExpiredJwtException.class })
    public ResponseEntity<Object> handleExpiredJwtException(Exception ex) {
        return this.buildResponse(HttpStatus.UNAUTHORIZED, ex, "Your token has expired, please log in again");
    }

    @ExceptionHandler(value = { AuthenticationException.class })
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {
        return this.buildResponse(HttpStatus.UNAUTHORIZED, ex, "You are not logged in");
    }

    @ExceptionHandler(value = { BadCredentialsException.class })
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex) {
        return this.buildResponse(HttpStatus.UNAUTHORIZED, ex, "Wrong username or password");
    }

    @ExceptionHandler(value = { SignatureException.class, MalformedJwtException.class })
    public ResponseEntity<Object> handleSignatureException(Exception ex) {
        return this.buildResponse(HttpStatus.UNAUTHORIZED, ex, "JWT is malformated: not valid");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
