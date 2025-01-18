package com.skypro.starbank.model;

import java.util.List;

public class RuleSet {
    private String productId;
    private List<Rule> conditions;

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public List<Rule> getConditions() { return conditions; }
    public void setConditions(List<Rule> conditions) { this.conditions = conditions; }
}

