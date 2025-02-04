package com.skypro.starbank.service.bothandlers.telegram;

import com.skypro.starbank.service.BotService;
import com.skypro.starbank.service.bothandlers.BotHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("start")
public class StartMessageHandler implements BotHandler {
    private final BotService botService;
    private static final Logger logger = LoggerFactory.getLogger(StartMessageHandler.class);

    public StartMessageHandler(BotService botService) {
        this.botService = botService;
    }

    @Override
    public void handleMessage(long chatId, String messageText) {
        logger.debug("Отправка стартового сообщения пользователю {}", chatId);
        botService.sendMessage(chatId,"Привет! Я бот, который выдает рекомендации. Выбери команду из меню.\n" +
                "Можешь воспользоваться справкой /help");
    }

    @Override
    public String getDescription() {
        return "Стартовое сообщение";
    }
}
