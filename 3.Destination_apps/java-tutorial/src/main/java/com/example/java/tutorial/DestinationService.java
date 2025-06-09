package com.example.java.tutorial;


import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpDestination;
import io.micrometer.core.instrument.util.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Service class for handling communication with external applications
 * Uses SAP Cloud SDK to connect to destinations configured in SAP BTP
 */
@Service
public class DestinationService {
    private static final String DESTINATION_NAME = "destination-unsecured-app";
    private static final String REL_URL = "";

    /**
     * Retrieves data from another application using a configured destination
     *
     * @return String containing the response data from the target application
     * @throws RuntimeException if there's an error during HTTP communication
     */
    public String getAnotherAppData() {

        HttpDestination destination = DestinationAccessor.getLoader().tryGetDestination(DESTINATION_NAME)
                .get().asHttp();
        HttpClient client = HttpClientAccessor.getHttpClient(destination);
        HttpGet httpGet = new HttpGet(REL_URL);
        HttpResponse httpResponse = null;
        String responseString = "";
        try {
            httpResponse = client.execute(httpGet);
            responseString = IOUtils.toString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return responseString;
    }
}
