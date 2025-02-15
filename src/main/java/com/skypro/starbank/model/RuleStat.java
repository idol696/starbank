package com.skypro.starbank.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "rule_stats")
public class RuleStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("rule_id")
    @Column(name = "rule_id", nullable = false, unique = true)
    private Long ruleId;

    @Column(name = "count", nullable = false)
    private int count;

    public RuleStat() {}

    public RuleStat(Long ruleId, int count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public void increment() {
        this.count++;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleStat ruleStat = (RuleStat) o;
        return getCount() == ruleStat.getCount() && Objects.equals(id, ruleStat.id) && Objects.equals(getRuleId(), ruleStat.getRuleId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getRuleId(), getCount());
    }
}
