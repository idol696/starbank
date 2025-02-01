package com.skypro.starbank.service;

import com.skypro.starbank.model.Recommendation;
import com.skypro.starbank.model.RecommendationResponse;
import com.skypro.starbank.model.rules.RuleSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationServiceImpl.class);
    private final RuleService ruleService;

    public RecommendationServiceImpl(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @Override
    @Transactional(readOnly = true)
    public RecommendationResponse getRecommendations(String userId) {
        if (userId == null) {
            logger.error("Пользователь не найден, userId равно null");
            return new RecommendationResponse(null, List.of());
        }

        logger.debug("Получение рекомендаций для пользователя с ID: {}", userId);
        List<RuleSet> allRules = ruleService.getAllRules();
        List<Recommendation> recommendations = allRules.stream()
                .filter(ruleSet -> ruleService.checkRulesForUser(userId, ruleSet))
                .map(ruleSet -> {
                    logger.debug("Рекомендация для продукта {}: {}", ruleSet.getProductId(), ruleSet.getProductName());
                    return new Recommendation(
                            ruleSet.getProductName(),
                            ruleSet.getProductId().toString(),
                            ruleSet.getProductText());
                })
                .collect(Collectors.toList());

        logger.info("🔹 Пользователь {} получил {} рекомендаций.", userId, recommendations.size());
        return new RecommendationResponse(userId, recommendations);
    }
}