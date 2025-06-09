package com.leverx.imwrdo.helloworld.service.tenant;

/**
 * Service interface for managing tenant schemas.
 * This interface defines methods for initializing and dropping tenant schemas.
 */
public interface TenantDBContainerService {
    void createContainer(String tenantId);

    void deleteContainer(String instanceId);
}
