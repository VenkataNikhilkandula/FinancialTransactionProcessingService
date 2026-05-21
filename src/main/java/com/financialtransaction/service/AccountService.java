package com.financialtransaction.service;

import com.financialtransaction.dto.request.AccountRequestDTO;
import com.financialtransaction.dto.response.AccountResponseDTO;
import org.springframework.data.domain.Page;

public interface AccountService {

    AccountResponseDTO createAccount(AccountRequestDTO requestDTO);

    AccountResponseDTO getAccountById(Long id);

    AccountResponseDTO getAccountByAccountNumber(String accountNumber);

    Page<AccountResponseDTO> getAllAccounts(int page, int size);

    void deleteAccount(Long id);
}