package com.example.odata.explore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the OData Explorer application
 * This Spring Boot application serves as a proxy for OData services
 * configured as destinations in SAP BTP
 */
@SpringBootApplication
public class OdataExploreApplication {

    public static void main(String[] args) {
        SpringApplication.run(OdataExploreApplication.class, args);
    }

}
