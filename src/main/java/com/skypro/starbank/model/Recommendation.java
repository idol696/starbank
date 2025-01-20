package com.skypro.starbank.model;

import java.util.Objects;

public class Recommendation {
    private String name;
    private String id;
    private String text;

    public Recommendation(String name, String id, String text) {
        this.name = name;
        this.id = id;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getId(), that.getId()) && Objects.equals(getText(), that.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getId(), getText());
    }
}

