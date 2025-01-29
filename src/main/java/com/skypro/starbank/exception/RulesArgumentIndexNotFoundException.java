package com.skypro.starbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RulesArgumentIndexNotFoundException extends RuntimeException {

    public RulesArgumentIndexNotFoundException() {
        super("ArgumentIndexError");
    }
}
