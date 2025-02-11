package com.skypro.starbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    /**
     * Конструктор без параметров.
     */
    public UserNotFoundException() {
        super("User not found");
    }

    /**
     * Конструктор с кастомным сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}

