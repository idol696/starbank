package com.skypro.starbank.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.starbank.model.rules.Rule;
import com.skypro.starbank.model.rules.RuleSet;
import com.skypro.starbank.model.rules.RuleSetWrapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class RuleServiceImpl implements RuleService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConcurrentHashMap<String, List<Rule>> rulesMap = new ConcurrentHashMap<>();
    private static final String RULES_FILE = "./rules.json";

    public RuleServiceImpl() {
        loadRules();
    }

    private void loadRules() {
        try {
            File file = new File(RULES_FILE);
            if (file.exists()) {
                RuleSetWrapper wrapper = objectMapper.readValue(file, RuleSetWrapper.class);
                wrapper.getRules().forEach(set -> rulesMap.put(set.getProductId(), set.getConditions()));
            }
        } catch (IOException e) {
            System.err.println("❌ Ошибка загрузки правил: " + e.getMessage());
        }
    }

    @Override
    public List<RuleSet> getAllRules() {
        return rulesMap.entrySet().stream()
                .map(entry -> new RuleSet(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public void setRules(List<RuleSet> newRules) {
        rulesMap.clear();
        newRules.forEach(set -> rulesMap.put(set.getProductId(), set.getConditions()));
        saveRulesAsync();
    }

    @Override
    public RuleSet getRulesByProductId(String productId) {
        List<Rule> conditions = rulesMap.get(productId);
        return (conditions != null) ? new RuleSet(productId, conditions) : null;
    }

    // Обновление правил для конкретного продукта
    @Override
    public void updateRulesForProduct(String productId, List<Rule> newConditions) {
        if (rulesMap.containsKey(productId)) {
            rulesMap.put(productId, newConditions);
            saveRulesAsync();
        } else {
            throw new RuntimeException("❌ Ошибка: Продукт с ID " + productId + " не найден.");
        }
    }

    @Async
    public void saveRulesAsync() {
        try {
            RuleSetWrapper wrapper = new RuleSetWrapper();
            wrapper.setRules(getAllRules());
            objectMapper.writeValue(new File(RULES_FILE), wrapper);
            System.out.println("✅ Правила успешно сохранены.");
        } catch (IOException e) {
            System.err.println("Ошибка сохранения правил: " + e.getMessage());
        }
    }
}
