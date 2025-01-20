package com.skypro.starbank.model.rules;

import java.util.List;

public class RuleSetWrapper {
    private List<RuleSet> rules;

    public List<RuleSet> getRules() {
        return rules;
    }

    public void setRules(List<RuleSet> rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "RuleSetWrapper{" +
                "rules=" + rules +
                '}';
    }
}
