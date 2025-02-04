package com.skypro.starbank.bots;

import com.skypro.starbank.events.TelegramMessageEvent;
import com.skypro.starbank.service.RecommendationService;
import com.skypro.starbank.service.bothandlers.BotHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TelegramBotTest {

    @Mock
    private BotHandler startHandler;

    @Mock
    private BotHandler helpHandler;

    @Mock
    private RecommendationService recommendationService;

    private Map<String, BotHandler> handlers;

    @InjectMocks
    private TelegramBot telegramBot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        handlers = Map.of(
                "start", startHandler,
                "help", helpHandler
        );

        telegramBot = new TelegramBot("fake_token", "test_bot", handlers, recommendationService);
    }

    @Test
    void shouldHandleStartCommand() throws TelegramApiException {
        Update update = createMockUpdate("/start");

        telegramBot.onUpdateReceived(update);

        verify(startHandler, times(1)).handleMessage(anyLong(), eq("/start"));
    }

    @Test
    void shouldHandleUnknownCommandWithHelp() throws TelegramApiException {
        Update update = createMockUpdate("/unknown");

        telegramBot.onUpdateReceived(update);

        verify(helpHandler, times(1)).handleMessage(anyLong(), eq("/unknown"));
    }

    @Test
    void shouldHandleTelegramMessageEvent() throws TelegramApiException {
        TelegramMessageEvent event = mock(TelegramMessageEvent.class);

        telegramBot.handleTelegramMessageEvent(event);

        verify(event, times(1)).getMessage();
    }

    private Update createMockUpdate(String text) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(text);
        when(message.getChatId()).thenReturn(123456L);
        return update;
    }
}
