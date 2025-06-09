package com.leverx.imwrdo.helloworld.config;

import com.sap.cloud.sdk.cloudplatform.tenant.TenantAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * MultitenantDataSourceRouter is a custom implementation of AbstractRoutingDataSource.
 * It determines the current lookup key for the data source based on the tenant ID.
 * This allows for routing database operations to the appropriate tenant-specific data source.
 */
@Slf4j
public class MultitenantDataSourceRouter extends AbstractRoutingDataSource {

    /**
     * This method is called to determine the current lookup key for the data source.
     * It retrieves the tenant ID from the TenantAccessor and logs it.
     *
     * @return The tenant ID as the current lookup key.
     */
    @Override
    protected Object determineCurrentLookupKey() {
        log.debug("[Lookup key] Tenant ID: [{}]", TenantAccessor.getCurrentTenant().getTenantId());
        return TenantAccessor.getCurrentTenant().getTenantId();
    }
}
