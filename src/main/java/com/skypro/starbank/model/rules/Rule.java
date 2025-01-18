package com.skypro.starbank.model.rules;

import java.util.List;

public class Rule {
    private String type;
    private String productType;
    private boolean negate;
    private String operator;
    private double value;
    private List<Rule> conditions;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public boolean isNegate() { return negate; }
    public void setNegate(boolean negate) { this.negate = negate; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public List<Rule> getConditions() { return conditions; }
    public void setConditions(List<Rule> conditions) { this.conditions = conditions; }
}

