package com.skypro.starbank.service.bothandlers.telegram;

import com.skypro.starbank.service.TelegramBotServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BotHandlerTest {

    @Mock
    private TelegramBotServiceImpl botService;

    private StartMessageHandler startMessageHandler;
    private HelpMessageHandler helpMessageHandler;
    private RecommendationMessageHandler recommendationMessageHandler;

    private static final long CHAT_ID = 123456L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Создаем обработчики, передавая им моки botService
        startMessageHandler = new StartMessageHandler(botService);
        helpMessageHandler = new HelpMessageHandler(botService);
        recommendationMessageHandler = new RecommendationMessageHandler(botService);
    }

    @Test
    void shouldSendStartMessage() throws TelegramApiException {
        startMessageHandler.handleMessage(CHAT_ID, "/start");
        verify(botService, times(1)).sendMessage(eq(CHAT_ID), anyString());
    }

    @Test
    void shouldSendHelpMessage() throws TelegramApiException {
        helpMessageHandler.handleMessage(CHAT_ID, "/help");
        verify(botService, times(1)).sendMessage(eq(CHAT_ID), anyString());
    }

    @Test
    void shouldSendRecommendationWithValidUsername() {
        String messageText = "/recommend Test User";
        List<String> args = List.of("Test", "User");
        String formattedUsername = "Test.User";
        String recommendations = "Рекомендации для Test.User";

        when(botService.giveMessageArguments(messageText)).thenReturn(args);
        when(botService.getBotRecommendationByUsername(formattedUsername)).thenReturn(recommendations);

        recommendationMessageHandler.handleMessage(CHAT_ID, messageText);

        verify(botService, times(1)).sendMessage(eq(CHAT_ID), eq(recommendations));
    }

    @Test
    void shouldSendErrorForMissingUsername() {
        String messageText = "/recommend";

        when(botService.giveMessageArguments(messageText)).thenReturn(List.of());

        recommendationMessageHandler.handleMessage(CHAT_ID, messageText);

        verify(botService, times(1)).sendMessage(eq(CHAT_ID), contains("Введите имя пользователя после команды"));
    }
}
