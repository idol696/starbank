
package com.skypro.starbank.bots;

import com.skypro.starbank.events.TelegramMessageEvent;
import com.skypro.starbank.service.RecommendationService;
import com.skypro.starbank.service.bothandlers.BotHandler;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Реализация Telegram-бота для предоставления рекомендаций.
 */
@Component
public class TelegramBot extends TelegramLongPollingBot  {

    private final Map<String, BotHandler> handlers;
    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);
    private final String botUsername;


    /**
     * Конструктор класса TelegramBot.
     *
     * @param botToken                      Токен Telegram-бота, полученный от BotFather.
     * @param botUsername                   Имя пользователя бота.
     */
    public TelegramBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            Map<String, BotHandler> handlers, RecommendationService recommendationService) {
        super(botToken);
        this.botUsername = botUsername;
        this.handlers = handlers;
    }


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
        try {
            List<BotCommand> commands = handlers.entrySet().stream()
                    .map(entry -> new BotCommand("/" + entry.getKey(), entry.getValue().getDescription()))
                    .collect(Collectors.toList());

            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            logger.error("Ошибка:{}", e.getMessage());
        }
    }

    /**
     * Обрабатывает входящие обновления от Telegram.
     * Проверяет наличие текстового сообщения и обрабатывает команды через /.
     * Если команда не распознана обработчиком, отправляет на /help
     *
     * @param update Объект {@link Update}, представляющий входящее сообщение.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            String command = messageText.split(" ")[0].substring(1);
            //logger.debug("Получена команда {} для пользователя {}", command, chatId);
            try {
                handlers.getOrDefault(command, handlers.get("help")).handleMessage(chatId, messageText);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
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
