package com.financialtransaction.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequestDTO {

    @NotBlank(message = "Account holder name is required")
    @Size(min = 3, max = 100,
            message = "Account holder name must be between 3 and 100 characters")
    private String accountHolderName;

    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "^[0-9]{10,18}$",
            message = "Account number must contain only digits")
    private String accountNumber;

    @DecimalMin(value = "0.0",
            inclusive = true,
            message = "Initial balance cannot be negative")
    private BigDecimal initialBalance;
}