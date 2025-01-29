package com.skypro.starbank.model.rules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.starbank.exception.RulesArgumentIndexNotFoundException;
import com.skypro.starbank.exception.RulesArgumentSerializationException;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RuleSet ruleSet;

    @Enumerated(EnumType.STRING)
    @Column(name = "query", nullable = false)
    private RuleQueryType query;

    @Column(name = "arguments", columnDefinition = "TEXT")
    private String argumentsJson;

    @Column(name = "negate", nullable = false)
    private boolean negate;

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Rule() {}

    public Rule(RuleQueryType query, List<String> arguments, boolean negate) {
        this.query = query;
        this.negate = negate;
        setArguments(arguments);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RuleQueryType getQuery() { return query; }
    public void setQuery(RuleQueryType query) { this.query = query; }

    public boolean isNegate() { return negate; }
    public void setNegate(boolean negate) { this.negate = negate; }

    public String getArgument(int index) {
        if (getArguments().get(index).isEmpty()) {
            throw new RulesArgumentIndexNotFoundException();
        }
        return getArguments().get(index);
    }

    public List<String> getArguments() {
        try {
            return objectMapper.readValue(argumentsJson, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RulesArgumentSerializationException("Ошибка десериализации arguments: " + argumentsJson);
        }
    }

    public void setArguments(List<String> arguments) {
        try {
            this.argumentsJson = objectMapper.writeValueAsString(arguments);
        } catch (JsonProcessingException e) {
            throw new RulesArgumentSerializationException("Ошибка сериализации arguments: " + arguments);
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
