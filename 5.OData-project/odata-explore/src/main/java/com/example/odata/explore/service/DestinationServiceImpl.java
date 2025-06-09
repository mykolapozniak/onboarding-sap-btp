package com.example.odata.explore.service;


import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpDestination;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsible for making HTTP requests to OData services
 * Uses SAP Cloud SDK to connect to destinations configured in SAP BTP
 * Specifically handles communication with the Northwind OData sample service
 */
@Slf4j
@Service
public class DestinationServiceImpl implements DestinationService {

    private static final String DESTINATION_NAME = "Northwind";
    private static final String REL_URL = "/V4/Northwind/Northwind.svc/";

    /**
     * Retrieves data from the Northwind OData service via the configured destination
     *
     * @param request   The original HTTP request containing path information
     * @param allParams Map of all query parameters from the original request
     * @return String containing the OData service response or error message
     */
    @Override
    public String getData(HttpServletRequest request, Map<String, String> allParams) {
        try {

            HttpDestination destination = DestinationAccessor.getDestination(DESTINATION_NAME).asHttp();


            String requestUri = request.getRequestURI();
            String basePath = request.getContextPath() + "/";
            String relativePath = requestUri.substring(basePath.length());


            String query = allParams.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&"));

            String fullUrl = destination.getUri() + REL_URL + relativePath + (query.isEmpty() ? "" : "?" + query);
            log.info("Destination url: {}", fullUrl);

            HttpClient httpClient = HttpClientAccessor.getHttpClient(destination);
            HttpGet getRequest = new HttpGet(fullUrl);
            HttpResponse response = httpClient.execute(getRequest);

            return EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            return "Proxy error: " + e.getMessage();
        }
    }
}
