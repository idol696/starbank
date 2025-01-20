package com.skypro.starbank.model.rules;

import java.util.List;
import java.util.Objects;

public class Rule {
    private String type;
    private String productType;
    private boolean negate;
    private String operator;
    private Double value;
    private String compareTo;
    private String compareType;
    private List<Rule> conditions;

    public Rule() {}

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public boolean isNegate() { return negate; }
    public void setNegate(boolean negate) { this.negate = negate; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public String getCompareTo() { return compareTo; }
    public void setCompareTo(String compareTo) { this.compareTo = compareTo; }

    public String getCompareType() { return compareType; }
    public void setCompareType(String compareType) { this.compareType = compareType; }

    public List<Rule> getConditions() { return conditions; }
    public void setConditions(List<Rule> conditions) { this.conditions = conditions; }

    public boolean hasSubRules() {
        return conditions != null && !conditions.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return negate == rule.negate &&
                Objects.equals(type, rule.type) &&
                Objects.equals(productType, rule.productType) &&
                Objects.equals(operator, rule.operator) &&
                Objects.equals(value, rule.value) &&
                Objects.equals(compareTo, rule.compareTo) &&
                Objects.equals(compareType, rule.compareType) &&
                Objects.equals(conditions, rule.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, productType, negate, operator, value, compareTo, compareType, conditions);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "type='" + type + '\'' +
                ", productType='" + productType + '\'' +
                ", negate=" + negate +
                ", operator='" + operator + '\'' +
                ", value=" + value +
                ", compareTo='" + compareTo + '\'' +
                ", compareType='" + compareType + '\'' +
                ", conditions=" + conditions +
                '}';
    }
}
