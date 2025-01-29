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
    RuleSet getRulesByProductId(String productId);

    /**
     * Полная замена всех правил.
     * @param newRules Новый список правил.
     */
    RuleSet setRules(RuleSet newRules);

    /**
     * Обновление условий правил для конкретного продукта.
     *
     * @param productId     Id продукта.
     * @param newConditions Новый список условий.
     */
    void updateRulesForProduct(Long productId, RuleSet newConditions);


    /**
     * Проверка соответствия пользователя правилам продукта.
     * @param userId UUID пользователя.
     * @param ruleSet Набор правил.
     * @return true, если пользователь соответствует правилам.
     */
    boolean checkRulesForUser(String userId, RuleSet ruleSet);

    void deleteRuleSet(Long id);
}
