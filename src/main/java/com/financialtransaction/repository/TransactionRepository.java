package com.financialtransaction.repository;

import com.financialtransaction.entity.Transaction;
import com.financialtransaction.enums.TransactionStatus;
import com.financialtransaction.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends
        JpaRepository<Transaction, UUID>,
        JpaSpecificationExecutor<Transaction> {

    
    boolean existsByIdempotencyKey(String idempotencyKey);

    Optional<Transaction> findByIdempotencyKey(
            String idempotencyKey
    );

    
    Page<Transaction> findByStatus(
            TransactionStatus status,
            Pageable pageable
    );

    Page<Transaction> findByTransactionType(
            TransactionType transactionType,
            Pageable pageable
    );

    
    Page<Transaction> findByFromAccount_AccountNumberOrToAccount_AccountNumber(
            String fromAccountNumber,
            String toAccountNumber,
            Pageable pageable
    );

    
    Page<Transaction> findByAmountBetween(
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Pageable pageable
    );

    
    Page<Transaction> findByCreatedAtBetween(
            LocalDateTime fromDate,
            LocalDateTime toDate,
            Pageable pageable
    );

    
    Page<Transaction> findByStatusAndCreatedAtBetween(
            TransactionStatus status,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            Pageable pageable
    );

    
    List<Transaction> findTop10ByFromAccount_AccountNumberOrderByCreatedAtDesc(
            String accountNumber
    );

    
    long countByStatus(TransactionStatus status);

    long countByTransactionType(TransactionType transactionType);
}