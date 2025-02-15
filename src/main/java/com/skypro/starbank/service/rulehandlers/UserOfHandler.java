package com.skypro.starbank.service.rulehandlers;

import com.skypro.starbank.model.rules.Rule;
import com.skypro.starbank.repository.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class UserOfHandler extends RuleMasterHandler {

    public UserOfHandler(TransactionRepository transactionRepository) {
        super(transactionRepository);
    }

    @Override
    public boolean evaluate(String userId, Rule rule) {
        logger.info("✅ Вызов обработчика {} для пользователя {}", rule.getQuery(), userId);

        String productType = rule.getArgument(0);
        boolean result = hasProduct(userId, productType) != rule.isNegate();
        logger.debug("✅ USER_OF {} -> {}", productType, result);
        return result;
    }

    @Override
    public String getRuleKey() {
        return "USER_OF";
    }
}
