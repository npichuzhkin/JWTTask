package com.npichuzhkin.JWTTask.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestInfo = String.format("Request: %s %s from IP: %s",
                request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        logger.info(requestInfo);

        filterChain.doFilter(request, response);
    }

    public static void logSuccessfulLogin(String username) {
        logger.info("Successful login for user: {}", username);
    }

    public static void logFailedLogin() {
        logger.warn("Failed login attempt");
    }

    public static void logAccountLocked(String username) {
        logger.warn("Account locked for user: {}", username);
    }

    public static void logTokenGeneration(String username) {
        logger.info("JWT token generated for user: {}", username);
    }
}

