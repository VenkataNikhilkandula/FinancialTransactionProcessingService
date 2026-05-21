package com.financialtransaction.util;

import java.util.UUID;

public class IdempotencyUtil {

    private IdempotencyUtil() {
    }

    
    public static String generateIdempotencyKey() {

        return UUID.randomUUID().toString();
    }

    
    public static boolean isValid(String key) {

        return key != null
                && !key.trim().isEmpty()
                && key.length() >= 10;
    }
}