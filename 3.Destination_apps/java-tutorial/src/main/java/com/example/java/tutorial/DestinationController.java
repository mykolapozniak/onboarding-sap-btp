package com.example.java.tutorial;

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
@RequestMapping
public class DestinationController {
    private final DestinationService service;

    public DestinationController(DestinationService service) {
        this.service = service;
    }

    /**
     * Handles GET requests to the root endpoint
     * Fetches data from another application via the DestinationService
     * Requires user authentication with the "Display" scope
     *
     * @return ResponseEntity containing data from another app and HTTP status
     * @throws NotAuthorizedException if the user lacks the required "Display" scope
     */
    @GetMapping
    @PreAuthorize("hasRole('Viewer') or hasAuthority('Display')")
    public ResponseEntity<String> readAnotherAppData() {
        // Call the service to fetch data and return the response
        return ResponseEntity.ok(service.getAnotherAppData());
    }

}
