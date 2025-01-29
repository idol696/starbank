package com.skypro.starbank.service;

import com.skypro.starbank.model.rules.Rule;
import com.skypro.starbank.model.rules.RuleSet;
import com.skypro.starbank.repository.RuleSetRepository;
import com.skypro.starbank.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RuleServiceImpl implements RuleService {
    private static final Logger logger = LoggerFactory.getLogger(RuleServiceImpl.class);

    private final RuleSetRepository ruleSetRepository;
    private final TransactionRepository transactionRepository;

    public RuleServiceImpl(RuleSetRepository ruleSetRepository, TransactionRepository transactionRepository) {
        this.ruleSetRepository = ruleSetRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Получение всех правил из базы
     */
    @Override
    public List<RuleSet> getAllRules() {
        return ruleSetRepository.findAll();
    }

    /**
     * Получение конкретного набора правил по ID
     */
    @Override
    public RuleSet getRulesByProductId(String id) {
        return getRuleSetById(UUID.fromString(id)).orElse(new RuleSet());
    }


    private Optional<RuleSet> getRuleSetById(UUID id) {
        return ruleSetRepository.findByProductId(id);
    }


    /**
     * Создание нового набора правил
     */
    @Override
    @Transactional
    public RuleSet setRules(RuleSet ruleSet) {
        if (ruleSet == null) {
            throw new IllegalArgumentException("Получен пустой JSON");
        }
        // Данный блок - защита от бага Hibernate + Liquibase, касающийся авто-инкремента
        if (ruleSet.getRules() != null) {
            for (Rule rule : ruleSet.getRules()) {
                rule.setRuleSet(ruleSet);
                rule.setId(null);
                logger.info("Rule ID: {} -> RuleSet ID: {}", rule.getId(), ruleSet.getId());
            }
        }
        logger.info("Создан RuleSet: {}", ruleSet);

        return ruleSetRepository.save(ruleSet);
    }

    /**
     * Обновление существующего набора правил
     */
    @Override
    @Transactional
    public void updateRulesForProduct(String id, RuleSet updatedRuleSet) {
        ruleSetRepository.findById(UUID.fromString(id)).map(existingRuleSet -> {
            existingRuleSet.setProductId(updatedRuleSet.getProductId());
            existingRuleSet.setProductName(updatedRuleSet.getProductName());
            existingRuleSet.setProductText(updatedRuleSet.getProductText());
            existingRuleSet.setRules(updatedRuleSet.getRules());
            return ruleSetRepository.save(existingRuleSet);
        }).orElseThrow(() -> new RuntimeException("Набор правил с ID " + id + " не найден"));
    }

    /**
     * Удаление набора правил по ID
     */
    @Override
    @Transactional
    public void deleteRuleSet(UUID id) {
        ruleSetRepository.findByProductId(id).ifPresent(ruleSetRepository::delete);
    }

    /**
     * Проверка выполнения правил для пользователя
     */
    @Override
    public boolean checkRulesForUser(String userId, RuleSet ruleSet) {
        logger.debug("🔍 Проверка правил для пользователя {} по продукту {}", userId, ruleSet.getProductId());
        boolean result = ruleSet.getRules().stream().allMatch(rule -> evaluateRule(userId, rule));
        logger.debug("🎯 Результат проверки правил для пользователя {}: {}", userId, result);
        return result;
    }

    /**
     * Выполнение отдельного правила
     */
    private boolean evaluateRule(String userId, Rule rule) {
        logger.debug("🔎 Проверка условия: {} для пользователя {} (Аргументы: {})",
                rule.getQuery(), userId, rule.getArguments());

        return switch (rule.getQuery()) {
            case "ACTIVE_USER_OF" -> {
                String productType = rule.getArgument(0);
                boolean result = hasProductActive(userId, productType) != rule.isNegate();
                logger.debug("✅ ACTIVE_USER_OF {} -> {}", productType, result);
                yield result;
            }
            case "USER_OF" -> {
                String productType = rule.getArgument(0);
                boolean result = hasProduct(userId, productType) != rule.isNegate();
                logger.debug("✅ USER_OF {} -> {}", productType, result);
                yield result;
            }
            case "TRANSACTION_SUM_COMPARE" -> {
                String productType = rule.getArgument(0);
                String transactionType = rule.getArgument(1);
                String operator = rule.getArgument(2);
                double value = Double.parseDouble(rule.getArgument(3));

                double total = getTotalAmount(userId, productType, transactionType);
                boolean result = compare(total, operator, value);
                logger.debug("✅ TRANSACTION_SUM_COMPARE {} -> {} {} {} -> {}", productType, total, operator, value, result);
                yield result;
            }
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> {
                String productType = rule.getArgument(0);
                String operator = rule.getArgument(1);

                double totalDeposits = getTotalAmount(userId, productType, "DEPOSIT");
                double totalWithdrawals = getTotalAmount(userId, productType, "EXPENSE");

                boolean result = compare(totalDeposits, operator, totalWithdrawals);
                logger.debug("✅ TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW {} -> {} {} {} -> {}", productType, totalDeposits, operator, totalWithdrawals, result);
                yield result;
            }
            default -> {
                logger.warn("⚠ Неизвестное правило: {}", rule.getQuery());
                yield false;
            }
        };
    }

    private boolean hasProduct(String userId, String productType) {
        boolean result = transactionRepository.userHasProduct(userId, productType);
        logger.debug("📊 Пользователь {} {} продукт {}", userId, result ? "имеет" : "НЕ имеет", productType);
        return result;
    }

    private boolean hasProductActive(String userId, String productType) {
        boolean result = transactionRepository.userHasProductCount(userId, productType) >=5;
        logger.debug("📊 Пользователь активного продукта {} {} активный продукт {}", userId, result , productType);
        return result;
    }

    private double getTotalAmount(String userId, String productType, String transactionType) {
        double total = transactionType.equals("DEPOSIT")
                ? transactionRepository.getTotalDeposits(userId, productType)
                : transactionRepository.getTotalExpenses(userId, productType);
        logger.debug("💰 Общая сумма {} {} для пользователя {}: {}", transactionType, productType, userId, total);
        return total;
    }

    private boolean compare(double actual, String operator, double value) {
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
}
