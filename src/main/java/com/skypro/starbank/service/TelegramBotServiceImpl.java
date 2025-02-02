package com.skypro.starbank.service;

import com.skypro.starbank.events.MessageEvent;
import com.skypro.starbank.events.TelegramMessageEvent;
import com.skypro.starbank.exception.UserNotFoundException;
import com.skypro.starbank.model.RecommendationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.List;

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
        RecommendationResponse recommendations;
        try {
            recommendations = recommendationService.getRecommendationsByUserName(username);
        } catch (UserNotFoundException e) {
            return "Пользователь не найден, проверьте правильность написания имени и фамилии";
        }

        StringBuilder response = new StringBuilder();
        response.append("Здравствуйте ").append(username).append("\n");
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
    }


        public List<String> giveMessageArguments (String messageText){
            return Arrays.stream(messageText.split("[.\s|]+"))
                    .skip(1)
                    .filter(arg -> arg.matches("[a-zA-Zа-яА-Я0-9]+"))
                    .toList();
        }

        @Override
        public void sendMessage ( long chatId, String text){
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(text);

            eventPublisher.publishEvent(new TelegramMessageEvent(this, message));
        }
    }
