package com.example.java.tutorial;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Main Controller class for handling REST API requests
 * This controller provides endpoints for the root path of the application
 * It demonstrates the use of SAP BTP XSUAA authentication and authorization
 */
@RestController
@Slf4j
@RequestMapping
public class GreetingController {
    /**
     * Handles GET requests to the root endpoint
     * Returns a simple "Hello World!" message if the user has the required authorization
     *
     * @return ResponseEntity containing the response message and HTTP status
     * @throws NotAuthorizedException if the user lacks the required "Display" scope
     */
    @GetMapping("first")
    @PreAuthorize("hasRole('Viewer') or hasRole('Display')")
    public ResponseEntity<String> readFirst() {
        log.info("User has required scope: Display and read first page");
        return ResponseEntity.ok("Hello World!");
    }

    @GetMapping("second")
    @PreAuthorize("hasRole('Viewer') or hasRole('Display')")
    public ResponseEntity<String> readSecond() {

        log.info("User has required scope: Display and read second page");
        return ResponseEntity.ok("Hello World second!");
    }
}