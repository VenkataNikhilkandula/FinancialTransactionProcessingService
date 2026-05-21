package com.financialtransaction.service;

import com.financialtransaction.entity.Account;

import java.math.BigDecimal;

public interface FraudDetectionService {

    void validateTransaction(
            Account fromAccount,
            Account toAccount,
            BigDecimal amount
    );
}