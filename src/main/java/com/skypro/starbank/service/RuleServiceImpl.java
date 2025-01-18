package com.skypro.starbank.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.starbank.model.Product;
import com.skypro.starbank.model.Rule;
import com.skypro.starbank.model.RuleSet;
import com.skypro.starbank.repository.ProductRepository;
import com.skypro.starbank.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class RuleServiceImpl implements RuleService {
    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;
    private final ConcurrentHashMap<String, List<Rule>> rulesMap = new ConcurrentHashMap<>();
    private static final String RULES_FILE = "./rules.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RuleServiceImpl(ProductRepository productRepository, TransactionRepository transactionRepository) {
        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
        loadRules();
    }

    // Загружаем правила из файла
    private void loadRules() {
        try {
            File file = new File(RULES_FILE);
            if (file.exists()) {
                List<RuleSet> ruleSets = objectMapper.readValue(file, new TypeReference<>() {});
                ruleSets.forEach(set -> rulesMap.put(set.getProductId(), set.getConditions()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки правил: " + e.getMessage());
        }
    }

     public List<Product> getRecommendations(String userId) {
        return productRepository.findAllProducts().stream()
                .filter(product -> checkRules(userId, product.getId()))
                .collect(Collectors.toList());
    }

    private boolean checkRules(String userId, String productId) {
        List<Rule> rules = rulesMap.get(productId);
        if (rules == null) return false;

        return rules.stream().allMatch(rule -> evaluateRule(userId, rule));
    }

    private boolean evaluateRule(String userId, Rule rule) {
        switch (rule.getType()) {
            case "HAS_PRODUCT":
                return hasProduct(userId, rule.getProductType()) != rule.isNegate();
            case "SUM_DEPOSIT":
                return compare(transactionRepository.getTotalDeposits(userId, rule.getProductType()), rule.getOperator(), rule.getValue());
            case "SUM_EXPENSE":
                return compare(transactionRepository.getTotalExpenses(userId, rule.getProductType()), rule.getOperator(), rule.getValue());
            case "OR":
                return rule.getConditions().stream().anyMatch(r -> evaluateRule(userId, r));
            default:
                return false;
        }
    }

    private boolean hasProduct(String userId, String productType) {
        return transactionRepository.userHasProduct(userId, productType);
    }

    private boolean compare(double actual, String operator, double value) {
        return switch (operator) {
            case ">" -> actual > value;
            case ">=" -> actual >= value;
            case "<" -> actual < value;
            case "<=" -> actual <= value;
            default -> false;
        };
    }
}

