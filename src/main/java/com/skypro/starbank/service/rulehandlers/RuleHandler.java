package com.skypro.starbank.service.rulehandlers;

import com.skypro.starbank.model.rules.Rule;

public interface RuleHandler {
    boolean evaluate(String userId, Rule rule);
    String getRuleKey();
}

