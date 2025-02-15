package com.skypro.starbank.service;

import com.skypro.starbank.exception.UserIsNullException;
import com.skypro.starbank.exception.UserNotFoundException;
import com.skypro.starbank.model.Recommendation;
import com.skypro.starbank.model.RecommendationResponse;
import com.skypro.starbank.model.rules.RuleSet;
import com.skypro.starbank.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationServiceImpl.class);
    private final RuleService ruleService;
    private final UserRepository userRepository;

    public RecommendationServiceImpl(RuleService ruleService, UserRepository userRepository) {
        this.ruleService = ruleService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public RecommendationResponse getRecommendations(String userId) {
        if (userId == null) {
            logger.error("Пользователь не найден, userId равно null");
            throw new UserIsNullException();
        }
        logger.debug("Получение рекомендаций для пользователя с ID: {}", userId);
        List<RuleSet> allRules = ruleService.getAllRules();
        List<Recommendation> recommendations = allRules.stream()
                .filter(ruleSet -> ruleService.checkRulesForUser(userId, ruleSet))
                .map(ruleSet -> {
                    logger.info("Рекомендация для продукта {}: {}", ruleSet.getProductId(), ruleSet.getProductName());
                    ruleService.incrementRuleStat(ruleSet.getId());
                    return new Recommendation(
                            ruleSet.getProductName(),
                            ruleSet.getProductId().toString(),
                            ruleSet.getProductText());
                })
                .toList();

        logger.info("🔹 Пользователь {} получил {} рекомендаций.", userId, recommendations.size());
        return new RecommendationResponse(userId, recommendations);
    }

    @Override
    public RecommendationResponse getRecommendationsByUserName(String username) {
        return userRepository.findUserByName(username)
                .map(user -> getRecommendations(user.getId()))
                .orElseThrow(() -> {
                    logger.warn("Пользователь {} не найден!", username);
                    return new UserNotFoundException("Пользователь " + username + " не найден!");
                });
    }
}