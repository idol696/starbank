package com.skypro.starbank.service;

import com.skypro.starbank.model.RecommendationResponse;

public interface RecommendationService {
    /**
     * Получает рекомендации для пользователя по его транзакциям.
     * @param userId ID пользователя.
     * @return Объект с рекомендациями.
     */
    RecommendationResponse getRecommendations(String userId);
    RecommendationResponse getRecommendationsByUserName(String username);
}

