package com.skypro.starbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIsNullException extends RuntimeException {
    public UserIsNullException() {
        super("UserIsEmpty");
    }
}
