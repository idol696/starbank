package com.skypro.starbank.service.rulehandlers;

import com.skypro.starbank.model.rules.Rule;
import com.skypro.starbank.repository.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class ActiveUserOfHandler extends RuleMasterHandler {

    public ActiveUserOfHandler(TransactionRepository transactionRepository) {
        super(transactionRepository);
    }

    @Override
    public boolean evaluate(String userId, Rule rule) {
        String productType = rule.getArgument(0);
        boolean result = hasProductCount(userId, productType) >=5;
        logger.debug("✅ ACTIVE_USER_OF {} -> {}", productType, result);
        return result;
    }

    @Override
    public String getRuleKey() {
        return "ACTIVE_USER_OF";
    }
}
