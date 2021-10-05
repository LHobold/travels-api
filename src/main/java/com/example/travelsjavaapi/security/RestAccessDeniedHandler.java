package com.example.travelsjavaapi.security;

import com.example.travelsjavaapi.exceptions.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            AccessDeniedException e) throws IOException, ServletException {

        System.out.println("Access Denied");
        ApiError errorMessage = new ApiError(HttpStatus.UNAUTHORIZED, "You are not logged in", "Unauthorised");

        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ObjectMapper mapper = new ObjectMapper();
        String jsonError = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorMessage);
        httpServletResponse.getWriter().write(jsonError);

    }

}
