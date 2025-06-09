package com.leverx.imwrdo.helloworld.service.tenant.impl;

import com.leverx.imwrdo.helloworld.config.MultitenantDataSourceRouter;
import com.leverx.imwrdo.helloworld.service.tenant.TenantDataSourceService;
import com.sap.xsa.core.instancemanager.client.ImClientException;
import com.sap.xsa.core.instancemanager.client.ManagedServiceInstance;
import com.sap.xsa.core.instancemanager.client.ServiceManagerClient;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the TenantDataSourceService interface.
 * This service handles the creation and management of data sources for tenants.
 */
@Slf4j
@Getter
@Service
public class TenantDataSourceServiceImpl implements TenantDataSourceService {
    private final MultitenantDataSourceRouter multitenantDataSourceRouter;
    private final ServiceManagerClient serviceManagerClient;
    // Map to store data sources for each tenant
    private final Map<Object, Object> dataSourceMap = new HashMap<>();

    public TenantDataSourceServiceImpl(DataSource dataSource, ServiceManagerClient serviceManagerClient) {
        this.multitenantDataSourceRouter = (MultitenantDataSourceRouter) dataSource;
        this.serviceManagerClient = serviceManagerClient;
    }

    /**
     * Initializes data sources for existing managed instances in the service manager.
     * This method is called after the bean is constructed.
     */
    @PostConstruct
    public void setupExistingDataSources() {
        log.info("Initializing existing data sources from service manager");
        try {
            // Check if provider tenant exists in service manager
            if (serviceManagerClient.getManagedInstances() != null) {
                log.info("Instance found. Creating data sources ");
                for (ManagedServiceInstance serviceInstance : serviceManagerClient.getManagedInstances()) {
                    addDataSourceToMap(serviceInstance);
                }
                multitenantDataSourceRouter.setTargetDataSources(dataSourceMap);
                multitenantDataSourceRouter.afterPropertiesSet();
                log.info("Finished initializing existing data sources. DataSource map: [{}]", dataSourceMap);
            }
        } catch (Exception e) {
            log.error("Error initializing existing data sources", e);
        }
    }

    /**
     * Creates a data source for the given tenant ID.
     *
     * @param tenantId The ID of the tenant
     */
    @Override
    public void addDataSource(String tenantId) {
        try {
            ManagedServiceInstance serviceInstance = serviceManagerClient.getManagedInstance(tenantId);
            addDataSourceToMap(serviceInstance);
            multitenantDataSourceRouter.setTargetDataSources(dataSourceMap);
            multitenantDataSourceRouter.afterPropertiesSet();
            log.info("DataSource map: [{}]", dataSourceMap);
        } catch (ImClientException e) {
            log.error("Error retrieve managed service instance for tenantId: [{}]", tenantId, e);
        } catch (Exception e) {
            log.error("Error creating DataSource for tenantId: [{}]", tenantId, e);
        }
    }

    /**
     * Deletes the data source for the given tenant ID.
     *
     * @param tenantId The ID of the tenant
     */
    @Override
    public void removeDataSource(String tenantId) {
        try {
            log.info("Attempting to delete DataSource for tenantId: [{}]", tenantId);
            ManagedServiceInstance serviceInstance = serviceManagerClient.getManagedInstance(tenantId);
            if (serviceInstance != null) {
                log.info("Deleting DataSource for tenantId: [{}]", tenantId);
                dataSourceMap.remove(tenantId);
                multitenantDataSourceRouter.setTargetDataSources(dataSourceMap);
                multitenantDataSourceRouter.afterPropertiesSet();
                log.info("DataSource map after deletion: [{}]", dataSourceMap);
            }
        } catch (ImClientException e) {
            log.error("Error retrieve managed service instance for tenantId: [{}]", tenantId, e);
        } catch (Exception e) {
            log.error("Error creating DataSource for tenantId: [{}]", tenantId, e);
        }
    }

    /**
     * Updates the data source map with the given service instance.
     *
     * @param serviceInstance The managed service instance
     */
    private void addDataSourceToMap(ManagedServiceInstance serviceInstance) {
        String tenantId = serviceInstance.getId();
        log.info("Processing tenant ID: [{}]", tenantId);
        Map<String, Object> credentials = serviceInstance.getBinding().getCredentials();

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl((String) credentials.get("url"));
        dataSource.setUsername((String) credentials.get("user"));
        dataSource.setPassword((String) credentials.get("password"));
        dataSourceMap.put(tenantId, dataSource);
    }
}
