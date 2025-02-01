package com.skypro.starbank.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Представляет ответ с рекомендациями для пользователя.
 */
@Schema(description = "Ответ с рекомендациями для пользователя")
public class RecommendationResponse {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Schema(description = "Уникальный идентификатор пользователя", example = "user123")
    private String userId;

    /**
     * Список рекомендаций для пользователя.
     */
    @Schema(description = "Список рекомендаций для пользователя")
    private List<Recommendation> recommendations;


    /**
     * Конструктор для создания ответа с рекомендациями.
     * @param userId Уникальный идентификатор пользователя
     * @param recommendations Список рекомендаций для пользователя
     */
    public RecommendationResponse(String userId, List<Recommendation> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }


    /**
     * Возвращает уникальный идентификатор пользователя.
     * @return Уникальный идентификатор пользователя
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Устанавливает уникальный идентификатор пользователя.
     * @param userId Уникальный идентификатор пользователя
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Возвращает список рекомендаций для пользователя.
     * @return Список рекомендаций для пользователя
     */
    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    /**
     * Устанавливает список рекомендаций для пользователя.
     * @param recommendations Список рекомендаций для пользователя
     */
    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }
}

