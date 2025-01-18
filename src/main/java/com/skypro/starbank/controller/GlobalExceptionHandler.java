package com.skypro.starbank.controller;

import com.skypro.starbank.exception.RecommendationNotFoundException;
import com.skypro.starbank.exception.RulesNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RulesNotFoundException.class)
    public ResponseEntity<String> handleRulesNotFoundException(RulesNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecommendationNotFoundException.class)
    public ResponseEntity<String> handleRecommendationNotFoundException(RecommendationNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
