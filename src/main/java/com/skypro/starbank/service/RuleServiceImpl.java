package com.skypro.starbank.service;

import com.skypro.starbank.exception.RulesBadPostParameterException;
import com.skypro.starbank.exception.RulesNotFoundException;
import com.skypro.starbank.model.rules.Rule;
import com.skypro.starbank.model.rules.RuleSet;
import com.skypro.starbank.repository.RuleSetRepository;
import com.skypro.starbank.service.rulehandlers.RuleHandler;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RuleServiceImpl implements RuleService {


    private static final Logger logger = LoggerFactory.getLogger(RuleServiceImpl.class);

    private final RuleSetRepository ruleSetRepository;
    private final Map<String, RuleHandler> ruleHandlers;

    @Autowired
    public RuleServiceImpl(RuleSetRepository ruleSetRepository,
                           @Lazy Map<String, RuleHandler> ruleHandlers) {
        this.ruleSetRepository = ruleSetRepository;
        this.ruleHandlers = ruleHandlers;
    }

    @PostConstruct
    public void logRuleHandlers() {
        if (ruleHandlers == null || ruleHandlers.isEmpty()) {
            logger.error("🚨 Ошибка: ruleHandlers не загружены! Spring не подставил обработчики.");
        } else {
            logger.debug("📌 Загруженные обработчики: {}", ruleHandlers.keySet());
        }
    }

    /**
     * Получение всех правил из базы
     */
    @Override
    public List<RuleSet> getAllRules() {
        return List.copyOf(ruleSetRepository.findAll());
    }

    /**
     * Получение конкретного набора правил по ID
     */
    @Override
    public RuleSet getRulesByProductId(UUID id) {
        return ruleSetRepository.findByProductId(id).orElse(new RuleSet());
    }

    /**
     * Создание нового набора правил
     */
    @Override
    @Transactional
    public RuleSet setRules(RuleSet ruleSet) {
        if (ruleSet == null) {
            throw new RulesBadPostParameterException("EmptyJSON");
        }

        if (ruleSet.getRules() != null) {
            for (Rule rule : ruleSet.getRules()) {
                rule.setRuleSet(ruleSet);
                rule.setId(null);
                logger.debug("Rule ID: {} -> RuleSet ID: {}", rule.getId(), ruleSet.getId());
            }
        }
        logger.info("Создан RuleSet: {}", ruleSet);

        return ruleSetRepository.save(ruleSet);
    }

    /**
     * Удаление набора правил по ID
     */
    @Override
    @Transactional
    public RuleSet deleteRuleSet(Long id) {
        RuleSet ruleSet = ruleSetRepository.findById(id)
                .orElseThrow(() -> new RulesNotFoundException("Набор правил с ID " + id + " не найден"));
        ruleSetRepository.delete(ruleSet);
        return ruleSet;
    }

    /**
     * Проверка выполнения правил для пользователя
     */
    @Override
    public boolean checkRulesForUser(String userId, RuleSet ruleSet) {
        logger.debug("🔍 Проверка правил для пользователя {} по продукту {}", userId, ruleSet.getProductId());
        boolean result = ruleSet.getRules().stream()
                .allMatch(rule -> {
                    boolean ruleResult = evaluateRule(userId, rule);
                    if (!ruleResult) {
                        logger.debug("❌ Условие не выполнено, прерываем проверку: {}", rule.getQuery());
                    }
                    return ruleResult;
                });
        logger.debug("🎯 Результат проверки правил для пользователя {}: {}", userId, result);
        return result;
    }

    /**
     * Выполнение отдельного правила
     */
    private boolean evaluateRule(String userId, Rule rule) {
        RuleHandler handler = ruleHandlers.get(rule.getQuery());
        logger.debug("📌 Доступные обработчики: {}", ruleHandlers.keySet());

        logger.debug("🔎 Проверка условия: {} для пользователя {} (Аргументы: {})",
                rule.getQuery(), userId, rule.getArguments());

        if (handler == null) {
            logger.warn("⚠ Неизвестное правило: {}", rule.getQuery());
            return false;
        }


        return handler.evaluate(userId, rule);
    }
}
