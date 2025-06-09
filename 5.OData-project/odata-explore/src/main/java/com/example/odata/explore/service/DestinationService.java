package com.example.odata.explore.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * Service interface for handling HTTP requests to OData services
 * This interface defines the contract for retrieving data from destinations
 * configured in SAP BTP
 */
public interface DestinationService {
    String getData(HttpServletRequest request, Map<String, String> allParams);
}
