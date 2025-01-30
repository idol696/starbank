package com.skypro.starbank.service;

import com.skypro.starbank.model.RecommendationResponse;
import com.skypro.starbank.model.User;
import com.skypro.starbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TelegramRecommendationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecommendationService recommendationService;

    public String getRecommendationByUsername(String username) {
        Optional<User> optionalUser = userRepository.findUserByName(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getId() == null) {
                return "Пользователь не найден";
            }
            RecommendationResponse recommendations = recommendationService.getRecommendations(user.getId());

            StringBuilder response = new StringBuilder();
            response.append("Здравствуйте ").append(user.getName()).append("\n");
            response.append("Новые продукты для вас:\n");
            if (recommendations.getRecommendations().isEmpty()) {
                response.append("На данный момент нет доступных рекомендаций.");
            } else {
                for (var recommendation : recommendations.getRecommendations()) {
                    response.append("- ").append(recommendation.getName())
                            .append(": ").append(recommendation.getText())
                            .append("\n");
                }
            }
            return response.toString();

        } else {
            return "Пользователь не найден";
        }
    }
}