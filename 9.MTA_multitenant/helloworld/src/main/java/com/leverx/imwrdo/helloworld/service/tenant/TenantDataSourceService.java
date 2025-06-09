package com.leverx.imwrdo.helloworld.service.tenant;

/**
 * Interface for managing tenant data sources.
 * This service is responsible for creating and managing data sources for tenants.
 */
public interface TenantDataSourceService {
    void addDataSource(String tenantId);

    void removeDataSource(String tenantId);
}
