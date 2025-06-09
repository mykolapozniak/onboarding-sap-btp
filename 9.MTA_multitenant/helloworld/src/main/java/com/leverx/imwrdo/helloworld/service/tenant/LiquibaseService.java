package com.leverx.imwrdo.helloworld.service.tenant;

/**
 * Service interface for managing Liquibase operations for tenant schemas.
 * This interface defines methods for setting up tenant schemas using Liquibase.
 */
public interface LiquibaseService {
    void setupTenantSchema(String tenantId);
}
