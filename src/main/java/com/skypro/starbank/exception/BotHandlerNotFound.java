package com.skypro.starbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BotHandlerNotFound extends RuntimeException {
    public BotHandlerNotFound(String message) {
        super(message);
    }
}
