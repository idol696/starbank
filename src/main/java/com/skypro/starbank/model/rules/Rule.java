package com.skypro.starbank.model.rules;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "rules")
@JsonPropertyOrder({ "query", "arguments", "negate" })
public class Rule {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_set_id")
    private RuleSet ruleSet;

    @Column(name = "query", nullable = false)
    private String query;

    @Column(name = "arguments", columnDefinition = "TEXT")
    private String argumentsJson;

    @Column(name = "negate", nullable = false)
    private boolean negate;

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Rule() {}

    public Rule(String query, List<String> arguments, boolean negate) {
        this.query = query;
        this.negate = negate;
        setArguments(arguments);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public boolean isNegate() { return negate; }
    public void setNegate(boolean negate) { this.negate = negate; }

    public String getArgument(int index) {
        if (getArguments().get(index).isEmpty()) {
            throw new RuntimeException("Error index");
        }
        return getArguments().get(index);
    }

    public List<String> getArguments() {
        try {
            return objectMapper.readValue(argumentsJson, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка десериализации arguments: " + argumentsJson, e);
        }
    }

    public void setArguments(List<String> arguments) {
        try {
            this.argumentsJson = objectMapper.writeValueAsString(arguments);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации arguments: " + arguments, e);
        }
    }

    public RuleSet getRuleSet() { return ruleSet; }
    public void setRuleSet(RuleSet ruleSet) { this.ruleSet = ruleSet; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(id, rule.id);
    }


    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", query='" + query + '\'' +
                ", arguments=" + argumentsJson +
                ", negate=" + negate +
                '}';
    }
}
