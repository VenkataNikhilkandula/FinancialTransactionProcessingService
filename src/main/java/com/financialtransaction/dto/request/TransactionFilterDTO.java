package com.financialtransaction.dto.request;

import com.financialtransaction.enums.TransactionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionFilterDTO {

    private String accountNumber;

    private TransactionStatus status;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    private Integer page = 0;

    private Integer size = 10;

    private String sortBy = "createdAt";

    private String sortDirection = "DESC";
}