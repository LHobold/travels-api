package com.example.travelsjavaapi.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenAuthenticationService {
    // EXPIRATION_TIME = 15 minutes (test)
    static final Date EXPIRATION_DATE = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
    static final String SECRET = "R1hLMWPl612-BtlCR14_SlVTVmXfTyZT5M5DVdL8v4w_MRkSKcEZAAsP6NqUjtXzGCeNVecglYohxtUcd_wD21y4cVpWa0mLHwOdHanriKHckMbZX1easZY9RBZwixFUAupdWTbdYa3jLZQGvL9bHrMOdBOzUpH5BUOUfFUxcSE";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";
    static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    static void addAuthentication(HttpServletResponse response, String username) {

        // GENERATE TOKEN BASED ON USERNAME
        String JWT = Jwts.builder().setSubject(username).setExpiration(EXPIRATION_DATE)
                .signWith(KEY, SignatureAlgorithm.HS512).compact();

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);

        try {

            String token = String.format("{\"token\":\"%s\", \"expirationDate\":\"%s\"}", JWT,
                    EXPIRATION_DATE.toString());
            JSONObject tokenJson = new JSONObject(token);

            response.setContentType("application/json");
            response.getWriter().write(tokenJson.toString());
            response.getWriter().flush();
            response.getWriter().close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        if (token != null) {
            // faz parse do token
            String user = Jwts.parserBuilder().setSigningKey(KEY).build()
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
            }
        }
        return null;
    }

}
