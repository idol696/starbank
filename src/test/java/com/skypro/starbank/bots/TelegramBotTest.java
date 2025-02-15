package com.skypro.starbank.bots;

import com.skypro.starbank.events.TelegramMessageEvent;
import com.skypro.starbank.exception.BotHandlerNotFound;
import com.skypro.starbank.service.bothandlers.BotHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class TelegramBotTest {

    @Mock
    private BotHandler startHandler;

    @Mock
    private BotHandler helpHandler;

    @Mock
    private BotHandler recommendHandler;

    private TelegramBot telegramBot;
    private static final long CHAT_ID = 123456L;
    private static final String DEFAULT_COMMAND = "help";

    @BeforeEach
    void setUp() {
        when(startHandler.getCommand()).thenReturn("start");
        when(helpHandler.getCommand()).thenReturn("help");
        when(recommendHandler.getCommand()).thenReturn("recommend");

        Map<String, BotHandler> handlers = Stream.of(startHandler, helpHandler, recommendHandler)
                .collect(Collectors.toMap(BotHandler::getCommand, Function.identity()));

        telegramBot = new TelegramBot("fake_token", "test_bot",
                DEFAULT_COMMAND, List.of(startHandler, helpHandler, recommendHandler));
    }

    @Test
    void shouldHandleStartCommand() throws TelegramApiException {
        Update update = createMockUpdate("/start");

        telegramBot.onUpdateReceived(update);

        verify(startHandler, times(1)).handleMessage(eq(CHAT_ID), eq("/start"));
    }

    @Test
    void shouldHandleHelpCommand() throws TelegramApiException {
        Update update = createMockUpdate("/help");

        telegramBot.onUpdateReceived(update);

        verify(helpHandler, times(1)).handleMessage(eq(CHAT_ID), eq("/help"));
    }

    @Test
    void shouldHandleRecommendCommand() throws TelegramApiException {
        Update update = createMockUpdate("/recommend Test.User");

        telegramBot.onUpdateReceived(update);

        verify(recommendHandler, times(1)).handleMessage(eq(CHAT_ID), eq("/recommend Test.User"));
    }

    @Test
    void shouldHandleUnknownCommandWithDefaultHandler() throws TelegramApiException {
        Update update = createMockUpdate("/unknown");

        telegramBot.onUpdateReceived(update);

        verify(helpHandler, times(1)).handleMessage(eq(CHAT_ID), eq("/unknown"));
    }

    @Test
    void shouldThrowExceptionWhenHandlerNotFound() {
        // Создаём тестовый объект бота с `defaultCommand = null`
        telegramBot = new TelegramBot("fake_token", "test_bot", null, List.of(startHandler, helpHandler, recommendHandler));

        Update update = createMockUpdate("/invalid");

        // Проверяем, что выбрасывается `BotHandlerNotFound`
        Exception exception = assertThrows(
                BotHandlerNotFound.class,
                () -> telegramBot.onUpdateReceived(update)
        );

        assertEquals("invalid", exception.getMessage());
    }


    @Test
    void shouldHandleTelegramMessageEvent() throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(CHAT_ID));
        sendMessage.setText("Test Message");

        TelegramMessageEvent event = new TelegramMessageEvent(this, sendMessage);

        TelegramBot spyBot = spy(telegramBot);
        doReturn(null).when(spyBot).execute(any(SendMessage.class));

        spyBot.handleTelegramMessageEvent(event);
        verify(spyBot, times(1)).execute(sendMessage);
    }

    private Update createMockUpdate(String text) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(text);
        when(message.getChatId()).thenReturn(CHAT_ID);
        return update;
    }
}
