package com.skypro.starbank.service.rulehandlers;

import com.skypro.starbank.model.rules.Rule;
import com.skypro.starbank.repository.TransactionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component("ACTIVE_USER_OF")
public class ActiveUserOfHandler extends MasterHandler {

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
}
