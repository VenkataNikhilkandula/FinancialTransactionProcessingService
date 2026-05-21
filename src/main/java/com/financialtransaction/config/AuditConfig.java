package com.financialtransaction.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAsync
@EnableScheduling
public class AuditConfig {

    /*
     * Enables:
     * 1. Async audit logging
     * 2. Scheduler support
     *
     * Example:
     * @Async
     * public void saveAuditLog() {}
     *
     * Example Scheduler:
     * @Scheduled(fixedRate = 60000)
     * public void retryFailedTransactions() {}
     */
}