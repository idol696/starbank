package com.skypro.starbank.configuration;

import com.skypro.starbank.bots.TelegramBot;
import com.skypro.starbank.service.bothandlers.BotHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class TelegramBotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBot telegramBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(telegramBot);
        return botsApi;
    }

    @Bean
    public Map<String, BotHandler> botHandlersMap(List<BotHandler> botHandlers) {
        Map<String, BotHandler> handlers = botHandlers.stream()
                .collect(Collectors.toMap(BotHandler::getCommand, Function.identity()));

         System.out.println("📌 Загруженные команды: " + handlers.keySet());

        return handlers;
    }
}
