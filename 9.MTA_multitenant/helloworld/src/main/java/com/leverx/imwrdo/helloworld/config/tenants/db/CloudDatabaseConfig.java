package com.leverx.imwrdo.helloworld.config.tenants.db;

import com.leverx.imwrdo.helloworld.config.MultitenantDataSourceRouter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * Configuration class for setting up the DataSource for the application.
 */
@Configuration
@Profile("cloud")
public class CloudDatabaseConfig {

    @Bean
    public DataSource dataSource() {
        MultitenantDataSourceRouter multiTenantDataSourceRouter = new MultitenantDataSourceRouter();
        multiTenantDataSourceRouter.setTargetDataSources(new HashMap<>());
        return multiTenantDataSourceRouter;
    }

}

