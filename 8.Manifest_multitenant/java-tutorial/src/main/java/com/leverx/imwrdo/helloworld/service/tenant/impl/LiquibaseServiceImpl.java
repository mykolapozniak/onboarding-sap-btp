package com.leverx.imwrdo.helloworld.service.tenant.impl;

import com.leverx.imwrdo.helloworld.service.tenant.LiquibaseService;
import com.sap.xsa.core.instancemanager.client.ImClientException;
import com.sap.xsa.core.instancemanager.client.ManagedServiceInstance;
import com.sap.xsa.core.instancemanager.client.ServiceManagerClient;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link LiquibaseService} interface.
 * This service handles Liquibase operations for tenant schemas.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LiquibaseServiceImpl implements LiquibaseService {
    private static final String LIQUIBASE_MASTER_REL_PATH = "db/changelog/master.xml";

    private final ServiceManagerClient serviceManagerClient;

    /**
     * Sets up the schema for the given tenant ID using Liquibase.
     *
     * @param tenantId the ID of the tenant
     */
    @Override
    public void setupTenantSchema(String tenantId) {
        log.info("Setting up service instance for tenant [{}].", tenantId);

        try {
            // Set the schema for the connection
            Database db = getDatabaseInstance(tenantId);
            Liquibase liquibase = new Liquibase(LIQUIBASE_MASTER_REL_PATH, new ClassLoaderResourceAccessor(), db);
            liquibase.update(new Contexts(), new LabelExpression());
            log.info("Successfully set up schema for tenant [{}].", tenantId);
        } catch (LiquibaseException e) {
            log.error("Liquibase error while setting up schema for tenant [{}]: {}", tenantId, e.getMessage(), e);
            throw new RuntimeException("Failed to set up tenant schema due to Liquibase error.", e);
        } catch (ImClientException e) {
            log.error("Error while retrieving managed service instance for tenant [{}]: {}", tenantId, e.getMessage(), e);
        }
    }

    /**
     * Gets the database instance for the given connection.
     *
     * @param tenantId the ID of the tenant
     * @return the database instance
     * @throws DatabaseException if an error occurs while getting the database instance
     */
    private Database getDatabaseInstance(String tenantId) throws DatabaseException, ImClientException {
        ManagedServiceInstance instance = serviceManagerClient.getManagedInstance(tenantId);
        return DatabaseFactory
                .getInstance()
                .openDatabase(
                        (String) instance.getBinding().getCredentials().get("url"),
                        (String) instance.getBinding().getCredentials().get("user"),
                        (String) instance.getBinding().getCredentials().get("password"),
                        null,
                        new ClassLoaderResourceAccessor()
                );
    }
}
