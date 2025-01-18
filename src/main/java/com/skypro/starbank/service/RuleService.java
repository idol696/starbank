package com.skypro.starbank.service;

import com.skypro.starbank.model.Product;

import java.util.List;

public interface RuleService {
    List<Product> getRecommendations(String userId);
}
