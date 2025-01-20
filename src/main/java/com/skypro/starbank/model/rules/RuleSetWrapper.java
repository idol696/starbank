package com.skypro.starbank.model.rules;

import java.util.List;

// Класс для удобства заворота JSON в список "rules"
public class RuleSetWrapper {
    private List<RuleSet> rules;

    public List<RuleSet> getRules() {
        return rules;
    }

    public void setRules(List<RuleSet> rules) {
        this.rules = rules;
    }
}
