package com.skypro.starbank.service.bothandlers.telegram;

import com.skypro.starbank.service.BotService;
import com.skypro.starbank.service.bothandlers.BotHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class HelpMessageHandler implements BotHandler {
    private final BotService botService;
    private static final Logger logger = LoggerFactory.getLogger(HelpMessageHandler.class);

    public HelpMessageHandler(BotService botService) {
        this.botService = botService;
    }

    @Override
    public void handleMessage(long chatId, String messageText) throws TelegramApiException {
        logger.debug("Отправка справочного сообщения пользователю {}", chatId);
        botService.sendMessage(chatId,"Привет! Я бот, который выдает рекомендации.\n" +
                "Доступны команды /start, /help и /recommend\nДля того чтобы использовать получить\n" +
                "рекомендацию банковского продукта набери /recommend <username>, где <username> \n" +
                "может быть Именем Фамилией через пробел или точку. Регистр букв роли не играет.");
    }

    @Override
    public String getDescription() {
        return "Помощь";
    }

    @Override
    public String getCommand() {
        return "help";
    }
}
