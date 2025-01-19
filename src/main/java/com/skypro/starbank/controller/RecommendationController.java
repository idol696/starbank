package com.skypro.starbank.controller;

import com.skypro.starbank.model.RecommendationResponse;
import com.skypro.starbank.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public RecommendationResponse getRecommendations(@PathVariable String userId) {
        return recommendationService.getRecommendations(userId);
    }
}
