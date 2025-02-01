
package com.skypro.starbank.bot;

import com.skypro.starbank.service.TelegramRecommendationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация Telegram-бота для предоставления рекомендаций.
 */
@Component
public class RecommendationBot extends TelegramLongPollingBot {

    private final TelegramRecommendationService telegramRecommendationService;

    /**
     * Конструктор класса RecommendationBot.
     *
     * @param botToken                      Токен Telegram-бота, полученный от BotFather.
     * @param botUsername                   Имя пользователя бота.
     * @param telegramRecommendationService Сервис для получения рекомендаций.
     */
    public RecommendationBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            TelegramRecommendationService telegramRecommendationService) {
        super(botToken);
        this.telegramRecommendationService = telegramRecommendationService;
        this.botUsername = botUsername;
    }

    private final String botUsername;

    /**
     * Возвращает имя пользователя бота.
     *
     * @return имя пользователя бота
     */
    @Override
    public String getBotUsername() {
        return botUsername;
    }

    /**
     * Выполняет настройку команд бота после его инициализации.
     * Использует аннотацию {@link PostConstruct} для выполнения после создания бина.
     */
    @PostConstruct
    public void init() {
        setBotCommands();
    }

    /**
     * Устанавливает команды, доступные в меню бота.
     * Добавляет команды /start, /help, /recommend в меню.
     */
    private void setBotCommands() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Начать работу с ботом"));
        listOfCommands.add(new BotCommand("/help", "Получить справку"));
        listOfCommands.add(new BotCommand("/recommend", "Получить рекомендацию"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обрабатывает входящие обновления от Telegram.
     * Проверяет наличие текстового сообщения и обрабатывает команды /start, /help, /recommend.
     *
     * @param update Объект {@link Update}, представляющий входящее сообщение.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    sendStartMessage(chatId);
                    break;
                case "/help":
                    sendHelpMessage(chatId);
                    break;
                case "/recommend":
                    sendMessage(chatId, "Введите имя пользователя после команды /recommend, например: /recommend testuser");
                    break;
                default:
                    if (messageText.startsWith("/recommend ")) {
                        handleRecommendCommand(chatId, messageText);
                    } else {
                        sendMessage(chatId, "Неизвестная команда. Используйте команды из меню.");
                    }
            }
        }
    }


    /**
     * Отправляет приветственное сообщение пользователю.
     *
     * @param chatId Идентификатор чата пользователя.
     */
    private void sendStartMessage(long chatId) {
        String startMessage = "Привет! Я бот, который выдает рекомендации. Выбери команду из меню.";
        sendMessage(chatId, startMessage);
    }


    /**
     * Отправляет справочное сообщение пользователю.
     *
     * @param chatId Идентификатор чата пользователя.
     */
    private void sendHelpMessage(long chatId) {
        String helpMessage = "Используйте команду /recommend <username>, чтобы получить рекомендации.";
        sendMessage(chatId, helpMessage);
    }

    /**
     * Обрабатывает команду /recommend, извлекает имя пользователя и отправляет рекомендацию.
     *
     * @param chatId      Идентификатор чата пользователя.
     * @param messageText Текст сообщения пользователя.
     */
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

    /**
     * Отправляет текстовое сообщение пользователю.
     *
     * @param chatId Идентификатор чата пользователя.
     * @param text   Текст сообщения для отправки.
     */
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
