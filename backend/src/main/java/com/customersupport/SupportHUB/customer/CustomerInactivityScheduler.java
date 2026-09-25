package com.customersupport.SupportHUB.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Background Scheduler for automated customer account lifecycle management.
 * Checks for customer accounts that have been dormant for 6 consecutive months
 * (180 days without any login/activity) and automatically deactivates them.
 */
@Component
public class CustomerInactivityScheduler {

    private static final Logger log = LoggerFactory.getLogger(CustomerInactivityScheduler.class);
    private final CustomerService customerService;

    public CustomerInactivityScheduler(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Runs every day at 02:00 AM server time.
     * Evaluates all active customers against the 180-day inactivity threshold.
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduleCustomerInactivityCheck() {
        log.info("[InactivityScheduler] Starting automated daily customer inactivity check (180-day threshold)...");
        try {
            int deactivatedCount = customerService.deactivateInactiveCustomers(180);
            log.info("[InactivityScheduler] Customer inactivity check finished successfully. Deactivated {} accounts.", deactivatedCount);
        } catch (Exception ex) {
            log.error("[InactivityScheduler] Error executing automated customer inactivity check: {}", ex.getMessage(), ex);
        }
    }
}
