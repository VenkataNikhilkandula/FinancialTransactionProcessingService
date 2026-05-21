package com.financialtransaction.dto.response;

import com.financialtransaction.enums.TransactionStatus;
import com.financialtransaction.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDTO {

    private UUID transactionId;

    private String fromAccount;

    private String toAccount;

    private BigDecimal amount;

    private String currency;

    private TransactionType transactionType;

    private TransactionStatus status;

    private String idempotencyKey;

    private LocalDateTime createdAt;
}