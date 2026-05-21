package com.financialtransaction.util;

import com.financialtransaction.exception.InvalidTransactionException;

import java.math.BigDecimal;

public class ValidationUtil {

    private ValidationUtil() {
    }

    
    public static void validateAmount(BigDecimal amount) {

        if (amount == null) {

            throw new InvalidTransactionException(
                    "Transaction amount cannot be null"
            );
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidTransactionException(
                    "Transaction amount must be greater than zero"
            );
        }
    }

    
    public static void validateAccountNumber(
            String accountNumber
    ) {

        if (accountNumber == null
                || accountNumber.trim().isEmpty()) {

            throw new InvalidTransactionException(
                    "Account number cannot be empty"
            );
        }

        if (!accountNumber.matches("^[0-9]{10,18}$")) {

            throw new InvalidTransactionException(
                    "Invalid account number format"
            );
        }
    }

    
    public static void validateCurrency(
            String currency
    ) {

        if (currency == null
                || currency.trim().isEmpty()) {

            throw new InvalidTransactionException(
                    "Currency cannot be empty"
            );
        }

        if (!currency.matches("^[A-Z]{3}$")) {

            throw new InvalidTransactionException(
                    "Invalid currency format"
            );
        }
    }

    
    public static void validateDifferentAccounts(
            String fromAccount,
            String toAccount
    ) {

        if (fromAccount.equals(toAccount)) {

            throw new InvalidTransactionException(
                    "Sender and receiver accounts cannot be same"
            );
        }
    }
}