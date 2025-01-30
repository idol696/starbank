package com.skypro.starbank.service;
import com.skypro.starbank.model.rules.RuleSet;

import java.util.List;
import java.util.UUID;

public interface RuleService {


    /**
     * Получение всех правил.
     * @return Список всех наборов правил.
     */
    List<RuleSet> getAllRules();

    /**
     * Получение правил для конкретного продукта.
     * @param productId UUID продукта.
     * @return Набор правил для данного продукта.
     */
    RuleSet getRulesByProductId(UUID productId);

    /**
     * Полная замена всех правил.
     * @param newRules Новый список правил.
     */
    RuleSet setRules(RuleSet newRules);


    /**
     * Проверка соответствия пользователя правилам продукта.
     * @param userId UUID пользователя.
     * @param ruleSet Набор правил.
     * @return true, если пользователь соответствует правилам.
     */
    boolean checkRulesForUser(String userId, RuleSet ruleSet);

    RuleSet deleteRuleSet(Long id);
}
