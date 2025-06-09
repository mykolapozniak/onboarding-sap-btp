package com.leverx.imwrdo.helloworld.controller;

import com.sap.cloud.sdk.cloudplatform.tenant.TenantAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/helloworld")
public class HelloController {

    // Only for DEVELOPMENT. NOT FOR PRODUCTION!!!

    /**
     * This is a simple endpoint that returns a greeting message.
     *
     * @return A string containing the greeting message.
     */

    @GetMapping
    @PreAuthorize("hasRole('Manager')")
    public String getGreeting() {
        log.info("Tenant : {}", TenantAccessor.getCurrentTenant().getTenantId());
        return "Hello World!";// Retrieve the token

    }

}
