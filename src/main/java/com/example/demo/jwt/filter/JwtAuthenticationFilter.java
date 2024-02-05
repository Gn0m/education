package com.example.demo.jwt.filter;

import com.example.demo.jwt.util.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService service;
    private final JwtService jwtService;

    @Autowired
    public JwtAuthenticationFilter(UserDetailsService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = extractTokenFromRequest(request);

        if (token != null && validateToken(token)) {
            Authentication authentication = createAuthentication(token, request);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    private boolean validateToken(String token) {
        return token.startsWith("Bearer ");
    }


    private Authentication createAuthentication(String token,
                                                HttpServletRequest request) {
        String jwt = token.substring(7);

        UserDetails userDetails = extractUserDetailsFromToken(jwt);

        UsernamePasswordAuthenticationToken authToken = null;
        if (jwtService.validateToken(jwt, Objects.requireNonNull(userDetails))) {
            authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        }

        return authToken;
    }

    private UserDetails extractUserDetailsFromToken(String jwt) {
        String login = jwtService.extractLogin(jwt);
        UserDetails userDetails = null;
        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            userDetails = this.service.loadUserByUsername(login);
        }
        return userDetails;
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
