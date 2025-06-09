package com.leverx.imwrdo.helloworld.service.tenant.impl;


import com.leverx.imwrdo.helloworld.dto.SubscriptionRequestDto;
import com.leverx.imwrdo.helloworld.service.tenant.LiquibaseService;
import com.leverx.imwrdo.helloworld.service.tenant.TenantDBContainerService;
import com.leverx.imwrdo.helloworld.service.tenant.TenantDataSourceService;
import com.leverx.imwrdo.helloworld.service.tenant.TenantProvisioningService;
import com.sap.xsa.core.instancemanager.client.ImClientException;
import com.sap.xsa.core.instancemanager.client.ServiceManagerClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

/**
 * Implementation of the TenantProvisioningService interface.
 * This service handles tenant provisioning events such as subscription and deletion.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantProvisioningServiceImpl implements TenantProvisioningService {

    private static final String URL_PATTERN = "https://%s-mta-bookshop-approuter.cfapps.us10-001.hana.ondemand.com";


    private final TenantDBContainerService tenantDBContainerService;
    private final ServiceManagerClient serviceManagerClient;
    private final TenantDataSourceService tenantDataSourceService;
    private final LiquibaseService liquibaseService;

    @Value("${provider.tenant.id}")
    private String providerTenantId;

    /**
     * Initializes the data source for the provider tenant.
     * This method is called after the bean is constructed.
     *
     * @throws ImClientException if there is an error while initializing the data source
     */
    @PostConstruct
    public void createAndSetupProviderDatasource() throws ImClientException {
        log.info("Initializing data source for provider tenant: {}", providerTenantId);
        if (serviceManagerClient.getManagedInstance(providerTenantId) != null) {
            log.info("Provider tenant schema instance already exists.");
        } else {
            log.info("Provider tenant schema instance not found. Creating new instance...");
            tenantDBContainerService.createContainer(providerTenantId);
            liquibaseService.setupTenantSchema(providerTenantId);
            tenantDataSourceService.addDataSource(providerTenantId);
        }
    }

    /**
     * Handles tenant subscription events.
     * Initializes the tenant schema and returns the tenant subscription URL.
     *
     * @param requestBody the request body containing subscription details
     * @param tenantId    the ID of the tenant
     * @return the tenant subscription URL
     */
    @Override
    public String onSubscription(SubscriptionRequestDto requestBody, String tenantId) {
        tenantDBContainerService.createContainer(tenantId);
        liquibaseService.setupTenantSchema(tenantId);
        tenantDataSourceService.addDataSource(tenantId);
        return format(URL_PATTERN, requestBody);
    }

    /**
     * Handles tenant deletion events.
     * Drops the tenant schema.
     *
     * @param tenantId the ID of the tenant
     */
    @Override
    public void onDelete(String tenantId) {
        tenantDBContainerService.deleteContainer(tenantId);
        tenantDataSourceService.removeDataSource(tenantId);
    }




}
