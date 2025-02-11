package com.skypro.starbank.service;

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

/**
 * Реализация сервиса для работы с Telegram-ботом.
 * Этот класс отвечает за обработку запросов пользователя,
 * получение рекомендаций из сервиса рекомендаций и отправку сообщений через событие.
 */
@Service
public class TelegramBotServiceImpl implements BotService {

    /**
     * Логгер для записи информации о работе бота.
     */
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotServiceImpl.class);

    /**
     * Сервис для получения рекомендаций пользователю.
     */
    private final RecommendationService recommendationService;

    /**
     * Публикация событий для отправки сообщений через Telegram.
     */
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param recommendationService Сервис для получения рекомендаций.
     * @param eventPublisher        Публикатор событий для отправки сообщений.
     */
    public TelegramBotServiceImpl(RecommendationService recommendationService,
                                  ApplicationEventPublisher eventPublisher) {
        this.recommendationService = recommendationService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Получает рекомендации для пользователя по его имени.
     * Если пользователь не найден, выбрасывает UserNotFoundException.
     *
     * @param username Имя пользователя.
     * @return Строковое представление рекомендаций.
     * @throws UserNotFoundException Если пользователь не найден.
     */
    @Transactional
    public String getBotRecommendationByUsername(String username) throws UserNotFoundException {
        RecommendationResponse recommendations;
        try {
            recommendations = recommendationService.getRecommendationsByUserName(username);
        } catch (UserNotFoundException e) {
            // Логируем ошибку и перебрасываем её дальше
            logger.error("Пользователь {} не найден", username, e);
            throw new UserNotFoundException(String.format("Пользователь %s не найден", username));
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

    /**
     * Разбивает текст сообщения на список аргументов.
     * Удаляет лишние пробелы и символы пунктуации, оставляя только буквы и цифры.
     *
     * @param messageText Исходный текст сообщения.
     * @return Список аргументов из текста сообщения.
     */
    public List<String> giveMessageArguments(String messageText) {
        return Arrays.stream(messageText.split("[.\s|]+"))
                .skip(1)
                .filter(arg -> arg.matches("[a-zA-Zа-яА-Я0-9]+"))
                .toList();
    }

    /**
     * Отправляет сообщение через Telegram.
     * Создает объект {@link SendMessage} и публикует событие для отправки сообщения.
     *
     * @param chatId ID чата, куда нужно отправить сообщение.
     * @param text   Текст сообщения.
     */
    @Override
    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        eventPublisher.publishEvent(new TelegramMessageEvent(this, message));
    }
}