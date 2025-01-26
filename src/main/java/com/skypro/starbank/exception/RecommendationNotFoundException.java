package com.skypro.starbank.exception;

public class RecommendationNotFoundException extends RuntimeException {

    public RecommendationNotFoundException(String message) {
        super(message);
    }
}