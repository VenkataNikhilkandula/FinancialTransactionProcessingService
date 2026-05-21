package com.financialtransaction.scheduler;

import com.financialtransaction.entity.Transaction;
import com.financialtransaction.enums.TransactionStatus;
import com.financialtransaction.repository.TransactionRepository;
import com.financialtransaction.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FailedTransactionRetryScheduler {

    private final TransactionRepository transactionRepository;
    private final AuditService auditService;

    
    @Scheduled(fixedRate = 300000)
    public void retryFailedTransactions() {

        log.info("Retry Scheduler Started At : {}", LocalDateTime.now());

        
        List<Transaction> failedTransactions =
                transactionRepository.findAll()
                        .stream()
                        .filter(transaction ->
                                transaction.getStatus()
                                        == TransactionStatus.FAILED)
                        .toList();

        if (failedTransactions.isEmpty()) {

            log.info("No failed transactions found for retry");

            return;
        }

        log.info("Total Failed Transactions Found : {}",
                failedTransactions.size());

        for (Transaction transaction : failedTransactions) {

            try {

                log.info("Retrying Transaction : {}",
                        transaction.getTransactionId());

                
                transaction.setStatus(
                        TransactionStatus.PROCESSING
                );

                transactionRepository.save(transaction);

                

                boolean retrySuccess = simulateRetry();

                if (retrySuccess) {

                    transaction.setStatus(
                            TransactionStatus.SUCCESS
                    );

                    transactionRepository.save(transaction);

                    auditService.logTransactionEvent(
                            transaction.getTransactionId(),
                            "RETRY_SUCCESS",
                            "Failed transaction retried successfully"
                    );

                    log.info("Transaction Retry Success : {}",
                            transaction.getTransactionId());

                } else {

                    transaction.setStatus(
                            TransactionStatus.FAILED
                    );

                    transactionRepository.save(transaction);

                    auditService.logTransactionEvent(
                            transaction.getTransactionId(),
                            "RETRY_FAILED",
                            "Retry attempt failed"
                    );

                    log.error("Transaction Retry Failed : {}",
                            transaction.getTransactionId());
                }

            } catch (Exception ex) {

                log.error(
                        "Retry Exception For Transaction : {} , Error : {}",
                        transaction.getTransactionId(),
                        ex.getMessage()
                );

                transaction.setStatus(
                        TransactionStatus.FAILED
                );

                transactionRepository.save(transaction);

                auditService.logTransactionEvent(
                        transaction.getTransactionId(),
                        "RETRY_EXCEPTION",
                        ex.getMessage()
                );
            }
        }

        log.info("Retry Scheduler Completed At : {}",
                LocalDateTime.now());
    }

    
    private boolean simulateRetry() {

        

        return Math.random() > 0.3;
    }
}