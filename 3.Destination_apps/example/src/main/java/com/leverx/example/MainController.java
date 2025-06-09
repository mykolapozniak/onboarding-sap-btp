package com.leverx.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class MainController {
    /**
     * Handles GET requests to the root endpoint
     * Requires user authentication with the "Display" scope
     */
    @GetMapping
    public ResponseEntity<String> getAll() {
        return new ResponseEntity<String>("Hello From the second Application", HttpStatus.OK);
    }
}
