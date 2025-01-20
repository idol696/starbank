package com.skypro.starbank.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.starbank.model.rules.Rule;
import com.skypro.starbank.model.rules.RuleSet;
import com.skypro.starbank.model.rules.RuleSetWrapper;
import com.skypro.starbank.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class RuleServiceImpl implements RuleService {
    private static final Logger logger = LoggerFactory.getLogger(RuleServiceImpl.class);
    private static final String RULES_FILE = "rules.json";
    private static final String TEMP_RULES_FILE = "rules_tmp.json";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConcurrentHashMap<String, RuleSet> rulesMap = new ConcurrentHashMap<>();
    private final TransactionRepository transactionRepository;

    public RuleServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        loadRules();
    }

    private void loadRules() {
        File file = new File(RULES_FILE);
        if (file.exists()) {
            try {
                RuleSetWrapper wrapper = objectMapper.readValue(file, RuleSetWrapper.class);
                wrapper.getRules().forEach(set -> rulesMap.put(set.getProductId(), set));
                logger.info("✅ Правила загружены из rules.json. Всего правил: {}", rulesMap.size());
            } catch (IOException e) {
                logger.error("❌ Ошибка загрузки правил: {}", e.getMessage());
            }
        } else {
            logger.warn("⚠ Файл rules.json не найден. Используется пустой набор правил.");
        }
    }

    @Override
    public List<RuleSet> getAllRules() {
        logger.debug("📌 Получение всех правил. Количество: {}", rulesMap.size());
        return List.copyOf(rulesMap.values());
    }

    @Override
    public RuleSet getRulesByProductId(String productId) {
        logger.debug("📌 Получение правил для продукта ID: {}", productId);
        return rulesMap.get(productId);
    }

    @Override
    public void setRules(List<RuleSet> newRules) {
        rulesMap.clear();
        newRules.forEach(set -> rulesMap.put(set.getProductId(), set));
        logger.info("🔄 Обновлены все правила. Новое количество: {}", rulesMap.size());
        saveRulesAsync();
    }

    @Override
    public void updateRulesForProduct(String productId, List<Rule> newConditions) {
        RuleSet ruleSet = rulesMap.get(productId);
        if (ruleSet != null) {
            ruleSet.setConditions(newConditions);
            logger.info("🔄 Обновлены правила для продукта ID: {}", productId);
            saveRulesAsync();
        } else {
            logger.warn("⚠ Продукт с ID {} не найден. Обновление правил не выполнено.", productId);
        }
    }

    @Async
    public void saveRulesAsync() {
        File tempFile = new File(TEMP_RULES_FILE);
        File targetFile = new File(RULES_FILE);

        try {
            RuleSetWrapper wrapper = new RuleSetWrapper();
            wrapper.setRules(getAllRules());

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(tempFile, wrapper);
            Files.move(tempFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            logger.info("✅ Правила успешно сохранены в rules.json.");
        } catch (IOException e) {
            logger.error("❌ Ошибка сохранения правил: {}", e.getMessage());
        }
    }

    @Override
    public boolean checkRulesForUser(String userId, RuleSet ruleSet) {
        logger.debug("🔍 Проверка правил для пользователя {} по продукту {}", userId, ruleSet.getProductId());
        boolean result = ruleSet.getConditions().stream().allMatch(rule -> evaluateRule(userId, rule));
        logger.debug("🎯 Результат проверки правил для пользователя {}: {}", userId, result);
        return result;
    }

    private boolean evaluateRule(String userId, Rule rule) {
        logger.debug("🔎 Проверка условия: {} для пользователя {} (Тип: {}, Значение: {})",
                rule.getType(), userId, rule.getProductType(), rule.getValue());

        return switch (rule.getType()) {
            case "HAS_PRODUCT" -> {
                boolean result = hasProduct(userId, rule.getProductType()) != rule.isNegate();
                logger.debug("✅ HAS_PRODUCT {} -> {}", rule.getProductType(), result);
                yield result;
            }
            case "SUM_DEPOSIT" -> {
                double totalDeposits = getTotalDeposits(userId, rule.getProductType());
                boolean result = compare(totalDeposits, rule.getOperator(), getSafeValue(rule));
                logger.debug("✅ SUM_DEPOSIT {} -> {} {} {} -> {}",
                        rule.getProductType(), totalDeposits, rule.getOperator(), rule.getValue(), result);
                yield result;
            }
            case "SUM_EXPENSE" -> {
                double totalExpenses = getTotalExpenses(userId, rule.getProductType());
                boolean result = compare(totalExpenses, rule.getOperator(), getSafeValue(rule));
                logger.debug("✅ SUM_EXPENSE {} -> {} {} {} -> {}",
                        rule.getProductType(), totalExpenses, rule.getOperator(), rule.getValue(), result);
                yield result;
            }
            case "OR" -> {
                boolean result = rule.getConditions() != null && rule.getConditions().stream()
                        .anyMatch(subRule -> evaluateRule(userId, subRule));
                logger.debug("✅ OR-условие: {} -> {}", rule.getConditions(), result);
                yield result;
            }
            default -> {
                logger.warn("⚠ Неизвестное правило: {}", rule.getType());
                yield false;
            }
        };
    }


    private double getSafeValue(Rule rule) {
        return rule.getValue() != null ? rule.getValue() : 0.0;
    }

    private boolean hasProduct(String userId, String productType) {
        boolean result = transactionRepository.userHasProduct(userId, productType);
        logger.debug("📊 Пользователь {} {} продукт {}", userId, result ? "имеет" : "НЕ имеет", productType);
        return result;
    }

    private double getTotalDeposits(String userId, String productType) {
        double total = transactionRepository.getTotalDeposits(userId, productType);
        logger.debug("💰 Общая сумма пополнений {} для пользователя {}: {}", productType, userId, total);
        return total;
    }

    private double getTotalExpenses(String userId, String productType) {
        double total = transactionRepository.getTotalExpenses(userId, productType);
        logger.debug("💸 Общая сумма трат {} для пользователя {}: {}", productType, userId, total);
        return total;
    }

    private boolean compare(double actual, String operator, double value) {
        boolean result = switch (operator) {
            case ">" -> actual > value;
            case ">=" -> actual >= value;
            case "<" -> actual < value;
            case "<=" -> actual <= value;
            default -> false;
        };
        logger.debug("🔢 Сравнение: {} {} {} -> {}", actual, operator, value, result);
        return result;
    }
}
