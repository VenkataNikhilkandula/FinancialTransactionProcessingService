package com.financialtransaction.controller;

import com.financialtransaction.dto.request.CreditRequestDTO;
import com.financialtransaction.dto.request.DebitRequestDTO;
import com.financialtransaction.dto.request.TransferRequestDTO;
import com.financialtransaction.dto.response.ApiResponseDTO;
import com.financialtransaction.dto.response.TransactionResponseDTO;
import com.financialtransaction.enums.TransactionStatus;
import com.financialtransaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    
    @PostMapping("/debit")
    public ResponseEntity<ApiResponseDTO<TransactionResponseDTO>> debitMoney(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody DebitRequestDTO requestDTO) {

        TransactionResponseDTO response =
                transactionService.debit(idempotencyKey, requestDTO);

        ApiResponseDTO<TransactionResponseDTO> apiResponse =
                ApiResponseDTO.<TransactionResponseDTO>builder()
                        .success(true)
                        .message("Debit transaction successful")
                        .data(response)
                        .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
    
    @PostMapping("/credit")
    public ResponseEntity<ApiResponseDTO<TransactionResponseDTO>> creditMoney(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody CreditRequestDTO requestDTO) {

        TransactionResponseDTO response =
                transactionService.credit(idempotencyKey, requestDTO);

        ApiResponseDTO<TransactionResponseDTO> apiResponse =
                ApiResponseDTO.<TransactionResponseDTO>builder()
                        .success(true)
                        .message("Credit transaction successful")
                        .data(response)
                        .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
    
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponseDTO<TransactionResponseDTO>> transferMoney(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody TransferRequestDTO requestDTO) {

        TransactionResponseDTO response =
                transactionService.transfer(idempotencyKey, requestDTO);

        ApiResponseDTO<TransactionResponseDTO> apiResponse =
                ApiResponseDTO.<TransactionResponseDTO>builder()
                        .success(true)
                        .message("Transfer transaction successful")
                        .data(response)
                        .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
   
    @GetMapping("/{transactionId}")
    public ResponseEntity<ApiResponseDTO<TransactionResponseDTO>> getTransactionById(
            @PathVariable UUID transactionId) {

        TransactionResponseDTO response =
                transactionService.getTransactionById(transactionId);

        ApiResponseDTO<TransactionResponseDTO> apiResponse =
                ApiResponseDTO.<TransactionResponseDTO>builder()
                        .success(true)
                        .message("Transaction fetched successfully")
                        .data(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }
    
    @GetMapping("/history")
    public ResponseEntity<ApiResponseDTO<Page<TransactionResponseDTO>>> getTransactionHistory(

            @RequestParam(required = false) String accountNumber,

            @RequestParam(required = false) TransactionStatus status,

            @RequestParam(required = false) BigDecimal minAmount,

            @RequestParam(required = false) BigDecimal maxAmount,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime toDate,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "createdAt") String sortBy,

            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Page<TransactionResponseDTO> response =
                transactionService.getTransactionHistory(
                        accountNumber,
                        status,
                        minAmount,
                        maxAmount,
                        fromDate,
                        toDate,
                        page,
                        size,
                        sortBy,
                        sortDir
                );

        ApiResponseDTO<Page<TransactionResponseDTO>> apiResponse =
                ApiResponseDTO.<Page<TransactionResponseDTO>>builder()
                        .success(true)
                        .message("Transaction history fetched successfully")
                        .data(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }
    
    @PostMapping("/reverse/{transactionId}")
    public ResponseEntity<ApiResponseDTO<TransactionResponseDTO>> reverseTransaction(
            @PathVariable UUID transactionId) {

        TransactionResponseDTO response =
                transactionService.reverseTransaction(transactionId);

        ApiResponseDTO<TransactionResponseDTO> apiResponse =
                ApiResponseDTO.<TransactionResponseDTO>builder()
                        .success(true)
                        .message("Transaction reversed successfully")
                        .data(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }
}