package com.skypro.starbank.service.bothandlers;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface BotHandler {
    void handleMessage(long chatId, String messageText) throws TelegramApiException;
    String getDescription();
    String getCommand();
}
