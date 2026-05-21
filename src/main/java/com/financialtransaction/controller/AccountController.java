package com.financialtransaction.controller;

import com.financialtransaction.dto.request.AccountRequestDTO;
import com.financialtransaction.dto.response.AccountResponseDTO;
import com.financialtransaction.dto.response.ApiResponseDTO;
import com.financialtransaction.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<AccountResponseDTO>> createAccount(
            @Valid @RequestBody AccountRequestDTO requestDTO) {

        AccountResponseDTO response =
                accountService.createAccount(requestDTO);

        ApiResponseDTO<AccountResponseDTO> apiResponse =
                ApiResponseDTO.<AccountResponseDTO>builder()
                        .success(true)
                        .message("Account created successfully")
                        .data(response)
                        .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AccountResponseDTO>> getAccountById(
            @PathVariable Long id) {

        AccountResponseDTO response =
                accountService.getAccountById(id);

        ApiResponseDTO<AccountResponseDTO> apiResponse =
                ApiResponseDTO.<AccountResponseDTO>builder()
                        .success(true)
                        .message("Account fetched successfully")
                        .data(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<ApiResponseDTO<AccountResponseDTO>> getAccountByNumber(
            @PathVariable String accountNumber) {

        AccountResponseDTO response =
                accountService.getAccountByAccountNumber(accountNumber);

        ApiResponseDTO<AccountResponseDTO> apiResponse =
                ApiResponseDTO.<AccountResponseDTO>builder()
                        .success(true)
                        .message("Account fetched successfully")
                        .data(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Object>> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Object response = accountService.getAllAccounts(page, size);

        ApiResponseDTO<Object> apiResponse =
                ApiResponseDTO.builder()
                        .success(true)
                        .message("Accounts fetched successfully")
                        .data(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> deleteAccount(
            @PathVariable Long id) {

        accountService.deleteAccount(id);

        ApiResponseDTO<String> apiResponse =
                ApiResponseDTO.<String>builder()
                        .success(true)
                        .message("Account deleted successfully")
                        .data("Deleted")
                        .build();

        return ResponseEntity.ok(apiResponse);
    }
}