package com.skypro.starbank.service.rulehandlers;

import com.skypro.starbank.model.rules.Rule;
import com.skypro.starbank.repository.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class TransactionSumCompareHandler extends RuleMasterHandler {

    public TransactionSumCompareHandler(TransactionRepository transactionRepository) {
        super(transactionRepository);
    }

    @Override
    public boolean evaluate(String userId, Rule rule) {
        String productType = rule.getArgument(0);
        String transactionType = rule.getArgument(1);
        String operator = rule.getArgument(2);
        double value = Double.parseDouble(rule.getArgument(3));

        double total = getTotalAmount(userId, productType, transactionType);
        boolean result = compare(total, operator, value);
        logger.debug("✅ TRANSACTION_SUM_COMPARE {} -> {} {} {} -> {}", productType, total, operator, value, result);
        return result;
    }

    @Override
    public String getRuleKey() {
        return "TRANSACTION_SUM_COMPARE";
    }
}

