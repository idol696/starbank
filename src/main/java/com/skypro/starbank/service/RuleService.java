package com.skypro.starbank.service;

import com.skypro.starbank.model.Product;
import com.skypro.starbank.model.rules.RuleSet;

import java.util.List;

import java.util.List;

public interface RuleService {

    List<RuleSet> getAllRules();
    void setRules(List<RuleSet> newRules);
    void saveRulesAsync();
}

