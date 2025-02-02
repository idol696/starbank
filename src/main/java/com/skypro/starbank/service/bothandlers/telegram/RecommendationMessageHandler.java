package com.skypro.starbank.service.bothandlers.telegram;

import com.skypro.starbank.service.BotService;
import com.skypro.starbank.service.TelegramBotServiceImpl;
import com.skypro.starbank.service.bothandlers.BotHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("recommend")
public class RecommendationMessageHandler implements BotHandler {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationMessageHandler.class);
    private final BotService botService;

    public RecommendationMessageHandler(TelegramBotServiceImpl telegramBotServiceImpl) {
        this.botService = telegramBotServiceImpl;
    }

    @Override
    public void handleMessage(long chatId, String messageText) {
        logger.debug("Отправка сообщения рекомендаций пользователю {}", chatId);

        List<String> arguments = botService.giveMessageArguments(messageText);
        logger.debug("с аргументами: {}", arguments);
        if (arguments.isEmpty()) {
            botService.sendMessage(chatId,"Введите имя пользователя после команды" +
                    " /recommend, например: /recommend Test User");
            return;
        }
        String username = toUpperCase(arguments);
        String recommendations = botService.getBotRecommendationByUsername(username);

        botService.sendMessage(chatId,recommendations);

    }

    @Override
    public String getDescription() {
        return "Получить рекомендацию (Укажи /recommend <username>)";
    }

    private String toUpperCase(List<String> arguments) {
        return arguments.stream()
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining("."));
    }
}
