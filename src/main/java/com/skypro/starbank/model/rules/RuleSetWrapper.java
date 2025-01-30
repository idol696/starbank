package com.skypro.starbank.model.rules;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RuleSetWrapper {

    @JsonProperty("data") // Заворачиваем в "data"
    private List<RuleSet> data;

    public RuleSetWrapper(List<RuleSet> data) {
        this.data = data;
    }

    public List<RuleSet> getData() { return data; }


    @Override
    public String toString() {
        return "RuleSetWrapper{" +
                "rules=" + data +
                '}';
    }
}
