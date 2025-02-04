package com.skypro.starbank.service.rulehandlers;

import com.skypro.starbank.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

public abstract class RuleMasterHandler implements RuleHandler {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final TransactionRepository transactionRepository;

    public RuleMasterHandler(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    protected boolean compare(double actual, String operator, double value) {
        boolean result = switch (operator) {
            case ">" -> actual > value;
            case ">=" -> actual >= value;
            case "<" -> actual < value;
            case "<=" -> actual <= value;
            case "==" -> actual == value;
            case "!=" -> actual != value;
            default -> false;
        };
        logger.debug("🔢 Сравнение: {} {} {} -> {}", actual, operator, value, result);
        return result;
    }

    protected double getTotalAmount(String userId, String productType, String transactionType) {
        double total = transactionType.equals("DEPOSIT")
                ? transactionRepository.getTotalDeposits(userId, productType)
                : transactionRepository.getTotalExpenses(userId, productType);
        logger.debug("💰 Общая сумма {} {} для пользователя {}: {}", transactionType, productType, userId, total);
        return total;
    }

    protected int hasProductCount(String userId, String productType) {
        return transactionRepository.userHasProductCount(userId, productType);
    }

    @Cacheable(value = "rules", key = "#productId")
    protected boolean hasProduct(String userId, String productType) {
        return transactionRepository.userHasProduct(userId, productType);
    }
}
