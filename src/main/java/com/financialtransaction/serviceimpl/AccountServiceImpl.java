package com.financialtransaction.serviceimpl;

import com.financialtransaction.dto.request.AccountRequestDTO;
import com.financialtransaction.dto.response.AccountResponseDTO;
import com.financialtransaction.entity.Account;
import com.financialtransaction.enums.AccountStatus;
import com.financialtransaction.exception.ResourceNotFoundException;
import com.financialtransaction.repository.AccountRepository;
import com.financialtransaction.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO requestDTO) {

        Account account = Account.builder()
                .accountHolderName(requestDTO.getAccountHolderName())
                .accountNumber(requestDTO.getAccountNumber())
                .balance(
                        requestDTO.getInitialBalance() != null
                                ? requestDTO.getInitialBalance()
                                : BigDecimal.ZERO
                )
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        Account savedAccount = accountRepository.save(account);

        return modelMapper.map(savedAccount, AccountResponseDTO.class);
    }

    @Override
    public AccountResponseDTO getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with ID: " + id
                        ));

        return modelMapper.map(account, AccountResponseDTO.class);
    }

    @Override
    public AccountResponseDTO getAccountByAccountNumber(String accountNumber) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with account number: "
                                        + accountNumber
                        ));

        return modelMapper.map(account, AccountResponseDTO.class);
    }

    @Override
    public Page<AccountResponseDTO> getAllAccounts(int page, int size) {

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Account> accountPage =
                accountRepository.findAll(pageable);

        return accountPage.map(
                account -> modelMapper.map(
                        account,
                        AccountResponseDTO.class
                )
        );
    }

    @Override
    public void deleteAccount(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with ID: " + id
                        ));

        accountRepository.delete(account);
    }
}