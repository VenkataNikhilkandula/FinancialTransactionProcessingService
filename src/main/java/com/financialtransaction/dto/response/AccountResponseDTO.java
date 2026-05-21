package com.financialtransaction.dto.response;

import com.financialtransaction.enums.AccountStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponseDTO {

    private Long accountId;

    private String accountHolderName;

    private String accountNumber;

    private BigDecimal balance;

    private AccountStatus status;

    private LocalDateTime createdAt;
}