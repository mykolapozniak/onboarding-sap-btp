package com.leverx.imwrdo.helloworld.config;

import com.sap.xsa.core.instancemanager.client.ServiceManagerClient;
import com.sap.xsa.core.instancemanager.client.impl.servicemanager.ServiceManagerClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceManagerClientConfig {

    @Bean
    public ServiceManagerClient serviceManagerClient(
            @Value("${service.manager.instance.id}") final String serviceManagerInstanceId,
            @Value("${service.manager.url}") final String serviceManagerUrl,
            @Value("${service.manager.token.url}") final String serviceManagerTokenUrl,
            @Value("${service.manager.client.id}") final String serviceManagerClientId,
            @Value("${service.manager.client.secret}") final String serviceManagerClientSecret,
            @Value("${service.manager.offering.name}") final String serviceManagerOfferingName,
            @Value("${service.manager.plan.name}") final String serviceManagerServiceName
    ) {
        return new ServiceManagerClientImpl(
                serviceManagerInstanceId,
                serviceManagerUrl,
                serviceManagerTokenUrl,
                serviceManagerClientId,
                serviceManagerClientSecret,
                serviceManagerOfferingName,
                serviceManagerServiceName
        );
    }
}
