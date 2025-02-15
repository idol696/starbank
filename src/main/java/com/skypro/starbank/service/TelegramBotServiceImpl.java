package com.skypro.starbank.service;

import com.skypro.starbank.events.TelegramMessageEvent;
import com.skypro.starbank.model.RecommendationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Service
public class TelegramBotServiceImpl implements BotService {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotServiceImpl.class);
    private final RecommendationService recommendationService;
    private final ApplicationEventPublisher eventPublisher;

    public TelegramBotServiceImpl(RecommendationService recommendationService,
                                  ApplicationEventPublisher eventPublisher) {
        this.recommendationService = recommendationService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public String getBotRecommendationByUsername(String username) {
        RecommendationResponse recommendations = null;
        recommendations = recommendationService.getRecommendationsByUserName(username);
        return buildRecommendationMessage(username, recommendations);
    }

    /**
     * Формирует сообщение с рекомендациями для пользователя.
     */
    private String buildRecommendationMessage(String username, RecommendationResponse recommendations) {
        StringBuilder response = new StringBuilder()
                .append("Здравствуйте, ").append(username).append("!\n")
                .append("Новые продукты для вас:\n");

        if (recommendations.getRecommendations().isEmpty()) {
            response.append("На данный момент нет доступных рекомендаций.");
        } else {
            recommendations.getRecommendations().forEach(recommendation ->
                    response.append("- ").append(recommendation.getName())
                            .append(": ").append(recommendation.getText())
                            .append("\n")
            );
        }

        return response.toString();
    }

    /**
     * Разбирает аргументы сообщения, удаляя команду.
     */
    public List<String> giveMessageArguments(String messageText) {
        Pattern pattern = Pattern.compile("\\b([a-zA-Zа-яА-Я0-9]+)\\b");
        Matcher matcher = pattern.matcher(messageText);
        return matcher.results()
                .skip(1)
                .map(MatchResult::group)
                .toList();
    }

    @Override
    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        eventPublisher.publishEvent(new TelegramMessageEvent(this, message));
    }
}
