package com.financialtransaction.util;

import java.util.UUID;

public class TransactionIdGenerator {

    private TransactionIdGenerator() {
    }

    
    public static UUID generateTransactionId() {

        return UUID.randomUUID();
    }
}