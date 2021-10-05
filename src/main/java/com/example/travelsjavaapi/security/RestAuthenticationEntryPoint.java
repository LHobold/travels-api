package com.example.travelsjavaapi.security;

import com.example.travelsjavaapi.exceptions.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            AuthenticationException e) throws IOException, ServletException {

        ApiError errorMessage = new ApiError(HttpStatus.UNAUTHORIZED, "Access denied", "Unauthorised");

        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper mapper = new ObjectMapper();
        String jsonError = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorMessage);
        httpServletResponse.getWriter().write(jsonError);
    }

}
