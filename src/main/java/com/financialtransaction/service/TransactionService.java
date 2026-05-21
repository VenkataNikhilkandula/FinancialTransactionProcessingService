package com.financialtransaction.service;

import com.financialtransaction.dto.request.CreditRequestDTO;
import com.financialtransaction.dto.request.DebitRequestDTO;
import com.financialtransaction.dto.request.TransferRequestDTO;
import com.financialtransaction.dto.response.TransactionResponseDTO;
import com.financialtransaction.enums.TransactionStatus;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface TransactionService {

    TransactionResponseDTO debit(
            String idempotencyKey,
            DebitRequestDTO requestDTO);

    TransactionResponseDTO credit(
            String idempotencyKey,
            CreditRequestDTO requestDTO);

    TransactionResponseDTO transfer(
            String idempotencyKey,
            TransferRequestDTO requestDTO);

    TransactionResponseDTO getTransactionById(UUID transactionId);

    Page<TransactionResponseDTO> getTransactionHistory(
            String accountNumber,
            TransactionStatus status,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            int page,
            int size,
            String sortBy,
            String sortDir
    );

    TransactionResponseDTO reverseTransaction(UUID transactionId);
}