package com.financialtransaction.repository;

import com.financialtransaction.entity.TransactionAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionAuditRepository
        extends JpaRepository<TransactionAudit, Long> {

    long countByTransaction_TransactionId(
            UUID transactionId
    );
}