package com.npichuzhkin.JWTTask.controllers;

import com.npichuzhkin.JWTTask.dto.AuthRequestDTO;
import com.npichuzhkin.JWTTask.services.UserService;
import com.npichuzhkin.JWTTask.utils.JWTUtils;
import com.npichuzhkin.JWTTask.utils.LoggingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JWTUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody AuthRequestDTO authRequestDTO) {

        String token = null;

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (authRequestDTO.getUsername(), authRequestDTO.getPassword()));

            UserDetails userDetails = userService.loadUserByUsername(authRequestDTO.getUsername());
            LoggingFilter.logTokenGeneration(authRequestDTO.getUsername());
            token = jwtUtils.generateToken(userDetails);
            LoggingFilter.logSuccessfulLogin(authRequestDTO.getUsername());

        } catch (BadCredentialsException e) {
            LoggingFilter.logFailedLogin();
            userService.increaseFailedAttempts(authRequestDTO);

            if (userService.findByUserName(authRequestDTO.getUsername()).getFailedLoginAttempts() >= 3){
                userService.lockUser(authRequestDTO);
                LoggingFilter.logAccountLocked(authRequestDTO.getUsername());
            }
            throw new BadCredentialsException("Bad Credentials");
        }

        assert token != null;
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }
}

