package com.financialtransaction.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "fraud_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Long ruleId;

    @Column(name = "rule_name",
            nullable = false,
            unique = true)
    private String ruleName;

    @Column(name = "max_amount_limit",
            precision = 19,
            scale = 2)
    private BigDecimal maxAmountLimit;

    @Column(nullable = false)
    private Boolean enabled;
}