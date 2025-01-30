package com.skypro.starbank.model;

import jakarta.persistence.Column;

import java.util.Objects;

public class User {
    private String id;
    private String firstName;
    private String lastName;

    public User(String id, String firstName, String lastName) {}

    public User(String id, String firstName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() { return id; }
    // Геттер для name, который объединяет first_name и last_name
    public String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getName(), user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}

