package com.skypro.starbank.model.rules;

import java.util.List;
import java.util.Objects;

public class RuleSet {
    private String productId;
    private List<Rule> conditions;

    public RuleSet() {}

    public RuleSet(String productId, List<Rule> conditions) {
        this.productId = productId;
        this.conditions = conditions;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<Rule> getConditions() {
        return conditions;
    }

    public void setConditions(List<Rule> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleSet ruleSet = (RuleSet) o;
        return Objects.equals(productId, ruleSet.productId) &&
                Objects.equals(conditions, ruleSet.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, conditions);
    }

    @Override
    public String toString() {
        return "RuleSet{" +
                "productId='" + productId + '\'' +
                ", conditions=" + conditions +
                '}';
    }
}

