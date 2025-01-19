package com.skypro.starbank.controller;

import com.skypro.starbank.model.RecommendationResponse;
import com.skypro.starbank.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
  Контроллер для обработки запросов рекомендаций.
 */
@RestController
@RequestMapping("/recommendation")
@Tag(name = "Recommendation Controller", description = "API для получения рекомендаций для пользователей.")
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * Конструктор для RecommendationController.
     * @param recommendationService Сервис для получения рекомендаций.
     */
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Получает рекомендации для пользователя по его ID.
     * @param userId ID пользователя, для которого запрашиваются рекомендации.
     * @return {@link RecommendationResponse} Объект с рекомендациями.
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Получить рекомендации по ID пользователя.", description = "Возвращает рекомендации для указанного пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос, возвращает рекомендации",
                    content = @Content(schema = @Schema(implementation = RecommendationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным ID не найден", content = @Content)
    })
    public RecommendationResponse getRecommendations(@Parameter(description = "ID пользователя", required = true) @PathVariable String userId) {
        return recommendationService.getRecommendations(userId);
    }
}