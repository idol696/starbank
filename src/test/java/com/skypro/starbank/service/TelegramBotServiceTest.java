package com.skypro.starbank.service;

import com.skypro.starbank.events.TelegramMessageEvent;
import com.skypro.starbank.model.RecommendationResponse;
import com.skypro.starbank.model.Recommendation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TelegramBotServiceTest {

    @Mock
    private RecommendationService recommendationService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TelegramBotServiceImpl telegramBotService;

    private static final long CHAT_ID = 123456L;

    @BeforeEach
    void setUp() {
        telegramBotService = new TelegramBotServiceImpl(recommendationService, eventPublisher);
    }

    @Test
    void shouldSendMessageSuccessfully() {
        String text = "Привет, это тестовое сообщение!";

        telegramBotService.sendMessage(CHAT_ID, text);

        ArgumentCaptor<TelegramMessageEvent> eventCaptor = ArgumentCaptor.forClass(TelegramMessageEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture()); // ✅ Проверяем, что отправлено сообщение

        SendMessage sentMessage = eventCaptor.getValue().getMessage();
        assertEquals(String.valueOf(CHAT_ID), sentMessage.getChatId());
        assertEquals(text, sentMessage.getText());
    }

    @Test
    void shouldReturnRecommendationForUser() {
        String username = "Test.User";
        String id = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        Recommendation recommendation = new Recommendation("Кредит", id, "Лучший кредит для вас!");
        RecommendationResponse recommendationResponse = new RecommendationResponse(userId, List.of(recommendation));

        when(recommendationService.getRecommendationsByUserName(username)).thenReturn(recommendationResponse);

        String response = telegramBotService.getBotRecommendationByUsername(username);

        assertTrue(response.contains("Здравствуйте, Test.User"));
        assertTrue(response.contains("Новые продукты для вас:"));
        assertTrue(response.contains("Кредит: Лучший кредит для вас!"));

        verify(recommendationService, times(1)).getRecommendationsByUserName(username); // ✅ Проверяем вызов сервиса
    }

    @Test
    void shouldReturnNoRecommendationsForUser() {
        String username = "Test.User";
        String id = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        RecommendationResponse emptyResponse = new RecommendationResponse(userId, List.of());

        when(recommendationService.getRecommendationsByUserName(username)).thenReturn(emptyResponse);

        String response = telegramBotService.getBotRecommendationByUsername(username);

        assertTrue(response.contains("Здравствуйте, Test.User"));
        assertTrue(response.contains("На данный момент нет доступных рекомендаций."));

        verify(recommendationService, times(1)).getRecommendationsByUserName(username); // ✅ Проверяем вызов сервиса
    }

    @Test
    void shouldReturnUserNotFoundMessage() {
        String username = "Test.User";

        when(recommendationService.getRecommendationsByUserName(username))
                .thenReturn(null);

        assertNull(telegramBotService.getBotRecommendationByUsername("Test.User"));

        verify(recommendationService, times(1)).getRecommendationsByUserName(username); // ✅ Проверяем вызов сервиса
    }

    @Test
    void shouldParseArgumentsCorrectly() {
        String messageText = "/recommend Иван Иванов";
        List<String> args = telegramBotService.giveMessageArguments(messageText);

        assertEquals(List.of("Иван", "Иванов"), args);
    }

    @Test
    void shouldParseArgumentsWithDotSeparator() {
        String messageText = "/recommend Ivan.Ivanov";
        List<String> args = telegramBotService.giveMessageArguments(messageText);

        assertEquals(List.of("Ivan", "Ivanov"), args);
    }

    @Test
    void shouldReturnEmptyListWhenNoArguments() {
        String messageText = "/recommend";
        List<String> args = telegramBotService.giveMessageArguments(messageText);

        assertTrue(args.isEmpty());
    }
}
