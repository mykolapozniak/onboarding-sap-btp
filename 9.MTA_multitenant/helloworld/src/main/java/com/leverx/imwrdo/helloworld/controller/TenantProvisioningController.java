package com.leverx.imwrdo.helloworld.controller;

import com.leverx.imwrdo.helloworld.dto.SubscriptionRequestDto;
import com.leverx.imwrdo.helloworld.service.tenant.TenantProvisioningService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling tenant provisioning callbacks.
 * This controller listens for subscription and deletion events from the SAP BTP service manager.
 * It processes the incoming requests and delegates the logic to the TenantProvisioningService.
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/callback/v1.0/tenants/{tenantId}")
public class TenantProvisioningController {

    private final TenantProvisioningService tenantProvisioningService;

    /**
     * Handles tenant subscription events.
     * Initializes the tenant schema and returns the tenant subscription URL.
     *
     * @param tenantId    the ID of the tenant
     * @param requestBody the request body containing subscription details
     * @return the tenant subscription URL
     */
    @PutMapping
    public String onSubscription(@PathVariable String tenantId, @RequestBody SubscriptionRequestDto requestBody) {
        log.info("Received subscription request [{}].", requestBody);
        return tenantProvisioningService.onSubscription(requestBody, tenantId);
    }

    /**
     * Handles tenant deletion events.
     * Drops the tenant schema.
     *
     * @param tenantId the ID of the tenant
     */
    @DeleteMapping
    public void onDelete(@PathVariable String tenantId) {
        tenantProvisioningService.onDelete(tenantId);
    }

}
