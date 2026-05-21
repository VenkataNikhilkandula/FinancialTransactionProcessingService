package com.financialtransaction.serviceimpl;

import com.financialtransaction.entity.Account;
import com.financialtransaction.entity.FraudRule;
import com.financialtransaction.exception.FraudDetectionException;
import com.financialtransaction.repository.FraudRuleRepository;
import com.financialtransaction.service.FraudDetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FraudDetectionServiceImpl implements FraudDetectionService {

    private final FraudRuleRepository fraudRuleRepository;

    @Override
    public void validateTransaction(
            Account fromAccount,
            Account toAccount,
            BigDecimal amount
    ) {

        List<FraudRule> fraudRules =
                fraudRuleRepository.findByEnabledTrue();

        for (FraudRule rule : fraudRules) {

            if (rule.getMaxAmountLimit() != null
                    && amount.compareTo(rule.getMaxAmountLimit()) > 0) {

                throw new FraudDetectionException(
                        "Fraud rule triggered: "
                                + rule.getRuleName()
                );
            }
        }

        if (fromAccount != null
                && fromAccount.getStatus().name().equals("BLOCKED")) {

            throw new FraudDetectionException(
                    "Sender account is blocked"
            );
        }

        if (toAccount != null
                && toAccount.getStatus().name().equals("BLOCKED")) {

            throw new FraudDetectionException(
                    "Receiver account is blocked"
            );
        }
    }
}