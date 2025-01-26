package com.skypro.starbank.model.rules;
import java.util.List;
import java.util.Objects;

public class RuleSet {
    private String productId;
    private String name;
    private String description;
    private List<Rule> conditions;

    public RuleSet() {}

    public RuleSet(String productId, String name, String description, List<Rule> conditions) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.conditions = conditions;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Rule> getConditions() { return conditions; }
    public void setConditions(List<Rule> conditions) { this.conditions = conditions; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleSet ruleSet = (RuleSet) o;
        return Objects.equals(productId, ruleSet.productId) &&
                Objects.equals(name, ruleSet.name) &&
                Objects.equals(description, ruleSet.description) &&
                Objects.equals(conditions, ruleSet.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name, description, conditions);
    }

    @Override
    public String toString() {
        return "RuleSet{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", conditions=" + conditions +
                '}';
    }
}
