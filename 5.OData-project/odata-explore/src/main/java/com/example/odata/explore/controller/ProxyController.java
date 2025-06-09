package com.example.odata.explore.controller;

import com.example.odata.explore.service.DestinationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Main Controller class for handling REST API requests
 * This controller provides endpoints for the root path of the application
 * It demonstrates the use of SAP BTP XSUAA authentication and authorization
 */
@RestController
@RequestMapping
@AllArgsConstructor
public class ProxyController {
    final private DestinationService destinationService;

    /**
     * Handles all GET requests to any path
     * The "/**" path pattern catches all requests regardless of path depth
     *
     * @param request   The HTTP request object containing path information
     * @param allParams Map of all query parameters from the request URL
     * @return ResponseEntity with the OData service response and HTTP 200 status
     */
    @GetMapping("/**")
    public ResponseEntity<String> getData(HttpServletRequest request, @RequestParam Map<String, String> allParams) {
        return ResponseEntity.ok(destinationService.getData(request, allParams));
    }

}
