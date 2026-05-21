package com.financialtransaction.serviceimpl;

import com.financialtransaction.dto.request.CreditRequestDTO;
import com.financialtransaction.dto.request.DebitRequestDTO;
import com.financialtransaction.dto.request.TransferRequestDTO;
import com.financialtransaction.dto.response.TransactionResponseDTO;
import com.financialtransaction.entity.Account;
import com.financialtransaction.entity.Transaction;
import com.financialtransaction.enums.TransactionStatus;
import com.financialtransaction.enums.TransactionType;
import com.financialtransaction.exception.DuplicateTransactionException;
import com.financialtransaction.exception.InsufficientBalanceException;
import com.financialtransaction.exception.ResourceNotFoundException;
import com.financialtransaction.repository.AccountRepository;
import com.financialtransaction.repository.TransactionRepository;
import com.financialtransaction.service.AuditService;
import com.financialtransaction.service.FraudDetectionService;
import com.financialtransaction.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final FraudDetectionService fraudDetectionService;
    private final AuditService auditService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public TransactionResponseDTO debit(
            String idempotencyKey,
            DebitRequestDTO requestDTO
    ) {

        validateDuplicateRequest(idempotencyKey);

        Account account = accountRepository
                .findByAccountNumber(requestDTO.getAccountNumber())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found"
                        ));

        fraudDetectionService.validateTransaction(
                account,
                null,
                requestDTO.getAmount()
        );

        if (account.getBalance()
                .compareTo(requestDTO.getAmount()) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient account balance"
            );
        }

        account.setBalance(
                account.getBalance()
                        .subtract(requestDTO.getAmount())
        );

        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .transactionId(UUID.randomUUID())
                .fromAccount(account)
                .amount(requestDTO.getAmount())
                .currency(requestDTO.getCurrency())
                .transactionType(TransactionType.DEBIT)
                .status(TransactionStatus.SUCCESS)
                .idempotencyKey(idempotencyKey)
                .createdAt(LocalDateTime.now())
                .build();

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        auditService.logTransactionEvent(
                savedTransaction.getTransactionId(),
                "DEBIT_SUCCESS",
                "Debit transaction completed successfully"
        );

        return modelMapper.map(
                savedTransaction,
                TransactionResponseDTO.class
        );
    }

    @Override
    @Transactional
    public TransactionResponseDTO credit(
            String idempotencyKey,
            CreditRequestDTO requestDTO
    ) {

        validateDuplicateRequest(idempotencyKey);

        Account account = accountRepository
                .findByAccountNumber(requestDTO.getAccountNumber())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found"
                        ));

        fraudDetectionService.validateTransaction(
                null,
                account,
                requestDTO.getAmount()
        );

        account.setBalance(
                account.getBalance()
                        .add(requestDTO.getAmount())
        );

        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .transactionId(UUID.randomUUID())
                .toAccount(account)
                .amount(requestDTO.getAmount())
                .currency(requestDTO.getCurrency())
                .transactionType(TransactionType.CREDIT)
                .status(TransactionStatus.SUCCESS)
                .idempotencyKey(idempotencyKey)
                .createdAt(LocalDateTime.now())
                .build();

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        auditService.logTransactionEvent(
                savedTransaction.getTransactionId(),
                "CREDIT_SUCCESS",
                "Credit transaction completed successfully"
        );

        return modelMapper.map(
                savedTransaction,
                TransactionResponseDTO.class
        );
    }

    @Override
    @Transactional
    public TransactionResponseDTO transfer(
            String idempotencyKey,
            TransferRequestDTO requestDTO
    ) {

        validateDuplicateRequest(idempotencyKey);

        Account sender = accountRepository
                .findByAccountNumber(requestDTO.getFromAccount())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Sender account not found"
                        ));

        Account receiver = accountRepository
                .findByAccountNumber(requestDTO.getToAccount())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Receiver account not found"
                        ));

        fraudDetectionService.validateTransaction(
                sender,
                receiver,
                requestDTO.getAmount()
        );

        if (sender.getBalance()
                .compareTo(requestDTO.getAmount()) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient balance"
            );
        }

        sender.setBalance(
                sender.getBalance()
                        .subtract(requestDTO.getAmount())
        );

        receiver.setBalance(
                receiver.getBalance()
                        .add(requestDTO.getAmount())
        );

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = Transaction.builder()
                .transactionId(UUID.randomUUID())
                .fromAccount(sender)
                .toAccount(receiver)
                .amount(requestDTO.getAmount())
                .currency(requestDTO.getCurrency())
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.SUCCESS)
                .idempotencyKey(idempotencyKey)
                .createdAt(LocalDateTime.now())
                .build();

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        auditService.logTransactionEvent(
                savedTransaction.getTransactionId(),
                "TRANSFER_SUCCESS",
                "Transfer transaction completed successfully"
        );

        return modelMapper.map(
                savedTransaction,
                TransactionResponseDTO.class
        );
    }

    @Override
    public TransactionResponseDTO getTransactionById(
            UUID transactionId
    ) {

        Transaction transaction =
                transactionRepository.findById(transactionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found"
                                ));

        return modelMapper.map(
                transaction,
                TransactionResponseDTO.class
        );
    }

    @Override
    public Page<TransactionResponseDTO> getTransactionHistory(
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
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                sortDir.equalsIgnoreCase("asc")
                        ? Sort.by(sortBy).ascending()
                        : Sort.by(sortBy).descending()
        );

        Page<Transaction> transactions =
                transactionRepository.findAll(pageable);

        return transactions.map(
                transaction ->
                        modelMapper.map(
                                transaction,
                                TransactionResponseDTO.class
                        )
        );
    }

    @Override
    @Transactional
    public TransactionResponseDTO reverseTransaction(
            UUID transactionId
    ) {

        Transaction transaction =
                transactionRepository.findById(transactionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found"
                                ));

        if (transaction.getStatus()
                != TransactionStatus.SUCCESS) {

            throw new RuntimeException(
                    "Only successful transactions can be reversed"
            );
        }

        Account sender = transaction.getFromAccount();
        Account receiver = transaction.getToAccount();

        if (sender != null && receiver != null) {

            receiver.setBalance(
                    receiver.getBalance()
                            .subtract(transaction.getAmount())
            );

            sender.setBalance(
                    sender.getBalance()
                            .add(transaction.getAmount())
            );

            accountRepository.save(sender);
            accountRepository.save(receiver);
        }

        transaction.setStatus(TransactionStatus.REVERSED);

        Transaction updatedTransaction =
                transactionRepository.save(transaction);

        auditService.logTransactionEvent(
                updatedTransaction.getTransactionId(),
                "TRANSACTION_REVERSED",
                "Transaction reversed successfully"
        );

        return modelMapper.map(
                updatedTransaction,
                TransactionResponseDTO.class
        );
    }

    private void validateDuplicateRequest(
            String idempotencyKey
    ) {

        boolean exists =
                transactionRepository
                        .existsByIdempotencyKey(idempotencyKey);

        if (exists) {

            throw new DuplicateTransactionException(
                    "Duplicate transaction request detected"
            );
        }
    }
}