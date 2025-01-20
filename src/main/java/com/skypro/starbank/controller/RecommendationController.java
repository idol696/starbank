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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * Контроллер для обработки запросов рекомендаций.
 */
@RestController
@RequestMapping("/recommendation")
@Tag(name = "Recommendation Controller", description = "API для получения рекомендаций для пользователей.")
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * Конструктор для RecommendationController.
     *
     * @param recommendationService Сервис для получения рекомендаций.
     */
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Получает рекомендации для пользователя по его ID.
     *
     * @param userId ID пользователя, для которого запрашиваются рекомендации.
     * @return {@link ResponseEntity} с {@link RecommendationResponse} объектом, в котором может быть пустой список.
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Получить рекомендации по ID пользователя.", description = "Возвращает рекомендации для указанного пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос, возвращает рекомендации",
                    content = @Content(schema = @Schema(implementation = RecommendationResponse.class)))
    })
    public ResponseEntity<RecommendationResponse> getRecommendations(@Parameter(description = "ID пользователя", required = true) @PathVariable String userId) {
        RecommendationResponse response = recommendationService.getRecommendations(userId);
        if (response == null || response.getRecommendations() == null || response.getRecommendations().isEmpty()) {
            return ResponseEntity.ok(new RecommendationResponse(userId, Collections.emptyList()));
        } else {
            return ResponseEntity.ok(response);
        }
    }
}
