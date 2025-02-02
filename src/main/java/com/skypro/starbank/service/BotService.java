package com.skypro.starbank.service;

import java.util.List;

public interface BotService {
    /**
     * Отправляет приветственное сообщение пользователю.
     *
     * //@param chatId Идентификатор чата пользователя.
     * //@param text Сообщение, которое вы хотите отправить пользователю
     */
    void sendMessage(long chatId, String text);
    String getBotRecommendationByUsername(String username);
    List<String> giveMessageArguments(String messageText);
}
