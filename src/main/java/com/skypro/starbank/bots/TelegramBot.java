package com.skypro.starbank.bots;

import com.skypro.starbank.events.TelegramMessageEvent;
import com.skypro.starbank.exception.BotHandlerNotFound;
import com.skypro.starbank.exception.BotUpdateException;
import com.skypro.starbank.service.bothandlers.BotHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Реализация Telegram-бота для предоставления рекомендаций.
 */
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final Map<String, BotHandler> handlers;
    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);
    private final String botUsername;
    private final String defaultCommand;

    /**
     * Конструктор класса TelegramBot.
     *
     * @param botToken  Токен Telegram-бота, полученный от BotFather.
     * @param botUsername Имя пользователя бота.
     * @param botHandlers Список командных бинов, автоматически маппящихся по их команде.
     */
    public TelegramBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            @Value("${telegram.bot.default-command}") String defaultCommand,
            List<BotHandler> botHandlers) {
        super(botToken);
        this.botUsername = botUsername;
        this.defaultCommand = defaultCommand;

        this.handlers = botHandlers.stream()
                .collect(Collectors.toMap(BotHandler::getCommand, Function.identity()));
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    /**
     * Обрабатывает входящие обновления от Telegram.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText().trim();
            long chatId = update.getMessage().getChatId();

            if (!messageText.startsWith("/")) {
                logger.warn("Получено пустое или некорректное сообщение от {}: '{}'", chatId, messageText);
                return;
            }

            String commandKey = messageText.split(" ")[0].substring(1).toLowerCase();
            logger.debug("Получена команда '{}' от пользователя {}", commandKey, chatId);

            BotHandler handler = handlers.get(commandKey);

            // Если команды нет, пробуем `defaultCommand`
            if (handler == null) {
                logger.warn("⚠ Обработчик для команды '{}' не найден!", commandKey);
                handler = handlers.get(defaultCommand);
                if (handler == null) {
                    logger.error("🚨 Ошибка: Обработчик по умолчанию '{}' не найден!", defaultCommand);
                    throw new BotHandlerNotFound(commandKey);
                }
            }
            try {
                handler.handleMessage(chatId, messageText);
            } catch (TelegramApiException e) {
                logger.error("Ошибка при обработке команды '{}': {}", commandKey, e.getMessage());
                throw new BotUpdateException(e.getMessage());
            }
        }
    }

    /**
     *  Слушает события TelegramMessageEvent и отправляет сообщение в Telegram.
     */
    @EventListener
    public void handleTelegramMessageEvent(TelegramMessageEvent event) {
        try {
            execute(event.getMessage());
        } catch (TelegramApiException e) {
            logger.error("Ошибка при отправке сообщения: {}", e.getMessage());
        }
    }
}
