package com.financialtransaction.security;

import com.financialtransaction.exception.DuplicateTransactionException;
import com.financialtransaction.exception.InvalidTransactionException;
import com.financialtransaction.repository.TransactionRepository;
import com.financialtransaction.util.IdempotencyUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class IdempotencyInterceptor implements HandlerInterceptor {

    private final TransactionRepository transactionRepository;

    private static final String IDEMPOTENCY_HEADER =
            "Idempotency-Key";

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {

        
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            return true;
        }

        
        String idempotencyKey =
                request.getHeader(IDEMPOTENCY_HEADER);

        if (!IdempotencyUtil.isValid(idempotencyKey)) {

            throw new InvalidTransactionException(
                    "Missing or invalid Idempotency-Key header"
            );
        }

        
        boolean alreadyExists =
                transactionRepository
                        .existsByIdempotencyKey(idempotencyKey);

        if (alreadyExists) {

            throw new DuplicateTransactionException(
                    "Duplicate transaction request detected"
            );
        }

        return true;
    }
}