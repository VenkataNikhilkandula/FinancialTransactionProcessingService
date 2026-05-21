package com.financialtransaction.service;

import java.util.UUID;

public interface AuditService {

    void logTransactionEvent(
            UUID transactionId,
            String eventType,
            String message
    );
}