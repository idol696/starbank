package com.skypro.starbank.service;

import com.skypro.starbank.model.Recommendation;
import com.skypro.starbank.model.RecommendationResponse;
import com.skypro.starbank.model.rules.RuleSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationServiceImpl.class);
    private final RuleService ruleService;

    public RecommendationServiceImpl(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @Override
    public RecommendationResponse getRecommendations(String userId) {
        List<RuleSet> allRules = ruleService.getAllRules();

        List<Recommendation> recommendations = allRules.stream()
                .filter(ruleSet -> ruleService.checkRulesForUser(userId, ruleSet))
                .map(ruleSet -> new Recommendation(ruleSet.getName(), ruleSet.getProductId(), ruleSet.getDescription()))
                .toList();

        logger.info("🔹 Пользователь {} получил {} рекомендаций.", userId, recommendations.size());
        return new RecommendationResponse(userId, List.copyOf(recommendations));
    }
}
