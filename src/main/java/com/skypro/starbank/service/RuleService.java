package com.skypro.starbank.service;
import com.skypro.starbank.model.RuleStat;
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

    /**
     * Удаляет правило срабатываний для указанного правила.
     * Используется при удалении правила.
     *
     * @param id идентификатор динамического правила, которое нужно удалить
     */
    RuleSet deleteRuleSet(Long id);

    /**
     * Возвращает статистику срабатываний для всех правил.
     * Если правило никогда не срабатывало, оно будет присутствовать в списке со значением счетчика 0.
     *
     * @return список всех правил с их статистикой срабатываний
     */
    List<RuleStat> getRuleStats();

    /**
     * Увеличивает счетчик срабатываний для указанного правила.
     * Если правило ранее не срабатывало, создается новая запись с начальным значением счетчика 1.
     *
     * @param ruleId идентификатор правила, для которого нужно увеличить счетчик
     */
    void incrementRuleStat(Long ruleId);
}
