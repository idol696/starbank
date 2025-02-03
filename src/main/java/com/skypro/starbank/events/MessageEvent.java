package com.skypro.starbank.events;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageEvent {
    SendMessage getMessage();
}
