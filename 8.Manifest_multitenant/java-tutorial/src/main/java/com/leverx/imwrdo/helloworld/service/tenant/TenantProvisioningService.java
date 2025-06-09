package com.leverx.imwrdo.helloworld.service.tenant;

import com.leverx.imwrdo.helloworld.dto.SubscriptionRequestDto;


/**
 * Service interface for tenant provisioning.
 * This interface defines methods for handling tenant subscription and deletion events.
 */
public interface TenantProvisioningService {
    String onSubscription(SubscriptionRequestDto requestBody, String tenantId);

    void onDelete(String tenantId);
}
