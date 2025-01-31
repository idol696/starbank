package com.skypro.starbank.bot;

import com.skypro.starbank.service.TelegramRecommendationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class RecommendationBot extends TelegramLongPollingBot {

    private final TelegramRecommendationService telegramRecommendationService;

    public RecommendationBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            TelegramRecommendationService telegramRecommendationService) {
        super(botToken);
        this.telegramRecommendationService = telegramRecommendationService;
        this.botUsername = botUsername;
    }

    private final String botUsername;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if ("/start".equals(messageText)) {
                sendHelpMessage(chatId);
            } else if (messageText.startsWith("/recommend")) {
                handleRecommendCommand(chatId, messageText);
            }
        }
    }

    private void sendHelpMessage(long chatId) {
        String helpMessage = "Привет! Я бот, который выдает рекомендации.\n" +
                "Используйте команду /recommend <username>, чтобы получить рекомендации.";
        sendMessage(chatId, helpMessage);
    }

    private void handleRecommendCommand(long chatId, String messageText) {
        String[] parts = messageText.split(" ");
        if (parts.length != 2) {
            sendMessage(chatId, "Неверный формат команды. Используйте /recommend <username>");
            return;
        }

        String username = parts[1];
        String recommendation = telegramRecommendationService.getRecommendationByUsername(username);

        sendMessage(chatId, recommendation);
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
