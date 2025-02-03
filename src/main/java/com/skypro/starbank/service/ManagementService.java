package com.skypro.starbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления статистикой срабатывания правил рекомендаций.
 * Предоставляет методы для увеличения счетчика срабатываний правил,
 * получения статистики по всем правилам, удаления статистики при удалении правила
 * и очистки кэшей.
 */
@Service
public class ManagementService {

    @Autowired
    private RuleStatisticRepository ruleStatisticRepository; //todo репозиторий @Roman

    /**
     * Увеличивает счетчик срабатываний для указанного правила.
     * Если правило ранее не срабатывало, создается новая запись с начальным значением счетчика 1.
     *
     * @param ruleId идентификатор правила, для которого нужно увеличить счетчик
     */
    public void incrementRuleStatistic(String ruleId) {
        RuleStatistic ruleStatistic = ruleStatisticRepository.findById(ruleId) //todo сущность статистики @Roman
                .orElse(new RuleStatistic(ruleId, 0));
        ruleStatistic.incrementCount();
        ruleStatisticRepository.save(ruleStatistic);
    }

    /**
     * Возвращает статистику срабатываний для всех правил.
     * Если правило никогда не срабатывало, оно будет присутствовать в списке со значением счетчика 0.
     *
     * @return список всех правил с их статистикой срабатываний
     */
    public List<RuleStatistic> getAllRuleStatistics() {
        return ruleStatisticRepository.findAll();
    }

    /**
     * Удаляет статистику срабатываний для указанного правила.
     * Используется при удалении правила.
     *
     * @param ruleId идентификатор правила, статистику которого нужно удалить
     */
    public void deleteRuleStatistic(String ruleId) {
        ruleStatisticRepository.deleteById(ruleId);
    }

    /**
     * Очищает все кэши и обновляет базу данных.
     */
    public void clearCaches() {
        ruleStatisticRepository.flush();
    }
}