package com.skypro.starbank.controller;

import com.skypro.starbank.exception.RecommendationNotFoundException;
import com.skypro.starbank.exception.RulesNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Tag(name = "Global Exception Handler", description = "API для обработки исключений")
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