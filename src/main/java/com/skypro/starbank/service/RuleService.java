package com.skypro.starbank.service;

import com.skypro.starbank.model.rules.Rule;
import com.skypro.starbank.model.rules.RuleSet;

import java.util.List;

public interface RuleService {
    List<RuleSet> getAllRules();
    RuleSet getRulesByProductId(String productId);
    void setRules(List<RuleSet> newRules);
    void updateRulesForProduct(String productId, List<Rule> newConditions);
    void saveRulesAsync();
}


