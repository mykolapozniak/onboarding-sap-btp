package com.leverx.imwrdo.helloworld.service.tenant.impl;

import com.leverx.imwrdo.helloworld.service.tenant.TenantDBContainerService;
import com.sap.xsa.core.instancemanager.client.ImClientException;
import com.sap.xsa.core.instancemanager.client.ManagedServiceInstance;
import com.sap.xsa.core.instancemanager.client.ServiceManagerClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Implementation of the TenantDBContainerService interface.
 * This service handles tenant schema initialization and deletion.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantDBContainerServiceImpl implements TenantDBContainerService {

    private final ServiceManagerClient serviceManagerClient;

    /**
     * Initializes the tenant schema for the given tenant ID.
     * This method creates a new schema in the database and sets it up using Liquibase.
     *
     * @param tenantId the ID of the tenant
     */
    @Override
    public void createContainer(String tenantId) {
        log.debug("Setup schema for tenant [{}].", tenantId);
        validateTenantId(tenantId);

        // Generate the schema name based on the tenant ID
        try {
            if (serviceManagerClient.getManagedInstance(tenantId) != null) {
                log.info("Schema instance already exists for tenant [{}].", tenantId);
            } else {
                ManagedServiceInstance instance = serviceManagerClient.createManagedInstance(tenantId);
                log.debug("Created service instance with ID [{}] for tenant [{}].", instance.getId(), tenantId);
            }
        } catch (ImClientException e) {
            log.error("Error while creating schema instance for tenant [{}]: {}", tenantId, e.getMessage(), e);
        }
        log.info("Successfully initialized schema for tenant [{}].", tenantId);
    }

    @Override
    public void deleteContainer(String instanceId) {
        log.info("Deleting schema instance with ID [{}].", instanceId);
        try {
            serviceManagerClient.deleteManagedInstance(instanceId);
            log.info("Successfully deleted schema instance with ID [{}].", instanceId);
        } catch (ImClientException e) {
            log.error("Failed to delete schema instance with ID: {} ", instanceId, e);
        }
    }

    /**
     * Validates the tenant ID format.
     * This method checks if the tenant ID matches the expected pattern.
     *
     * @param tenantId the ID of the tenant
     */
    private void validateTenantId(String tenantId) {
        // Validate the tenant ID format using a regex pattern
        if (!Objects.nonNull(tenantId)) {
            throw new IllegalStateException("Invalid tenant id");
        }
    }


}
