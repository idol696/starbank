package com.skypro.starbank.events;

import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class TelegramMessageEvent extends ApplicationEvent implements MessageEvent {
    private final SendMessage message;

    public TelegramMessageEvent(Object source, SendMessage message) {
        super(source);
        this.message = message;
    }

    @Override
    public SendMessage getMessage() {
        return message;
    }
}