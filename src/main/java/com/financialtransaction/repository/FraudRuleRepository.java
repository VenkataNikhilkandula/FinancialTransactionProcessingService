package com.financialtransaction.repository;

import com.financialtransaction.entity.FraudRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FraudRuleRepository
        extends JpaRepository<FraudRule, Long> {

    
    List<FraudRule> findByEnabledTrue();

    
    FraudRule findByRuleName(String ruleName);

   
    boolean existsByRuleName(String ruleName);
}