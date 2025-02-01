package com.skypro.starbank.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

/**
 • Представляет пользователя системы StarBank.
 */
@Schema(description = "Информация о пользователе")
public class User {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Schema(description = "Уникальный идентификатор пользователя", example = "user123")
    private String id;

    /**
     * Имя пользователя.
     */
    @Schema(description = "Имя пользователя", example = "Иван Иванов")
    private String name;

    public User() {}

    /**
     * Конструктор для создания пользователя.
     * @param id Уникальный идентификатор пользователя
     * @param name Имя пользователя
     */
    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Возвращает уникальный идентификатор пользователя.
     * @return Уникальный идентификатор пользователя
     */
    public String getId() { return id; }

    /**
     * Возвращает имя пользователя.
     * @return Имя пользователя
     */
    public String getName() { return name; }

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