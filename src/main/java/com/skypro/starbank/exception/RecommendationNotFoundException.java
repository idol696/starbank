package com.skypro.starbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// DEPRECATED
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecommendationNotFoundException extends RuntimeException {

    public RecommendationNotFoundException(String message) {
        super(message);
    }
}