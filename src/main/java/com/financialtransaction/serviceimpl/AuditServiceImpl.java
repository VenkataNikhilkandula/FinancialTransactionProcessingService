package com.financialtransaction.serviceimpl;

import com.financialtransaction.entity.Transaction;
import com.financialtransaction.entity.TransactionAudit;
import com.financialtransaction.exception.ResourceNotFoundException;
import com.financialtransaction.repository.TransactionAuditRepository;
import com.financialtransaction.repository.TransactionRepository;
import com.financialtransaction.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final TransactionAuditRepository auditRepository;
    private final TransactionRepository transactionRepository;

    
    @Async
    @Override
    public void logTransactionEvent(
            UUID transactionId,
            String eventType,
            String message
    ) {

        try {

            
            Transaction transaction =
                    transactionRepository.findById(transactionId)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Transaction not found with ID : "
                                                    + transactionId
                                    ));

            
            TransactionAudit audit =
                    TransactionAudit.builder()
                            .transaction(transaction)
                            .eventType(eventType)
                            .message(message)
                            .createdAt(LocalDateTime.now())
                            .build();

            auditRepository.save(audit);

            log.info(
                    "Audit log saved successfully for transaction : {}",
                    transactionId
            );

        } catch (Exception ex) {

            log.error(
                    "Failed to save audit log for transaction : {} , Error : {}",
                    transactionId,
                    ex.getMessage()
            );
        }
    }
}