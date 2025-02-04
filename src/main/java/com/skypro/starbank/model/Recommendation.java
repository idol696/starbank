package com.skypro.starbank.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

/**
 • Представляет рекомендацию для пользователя.
 */
@Schema(description = "Рекомендация для пользователя")
public class Recommendation {

    /**
     * Название рекомендации.
     */
    @Schema(description = "Название рекомендации", example = "Инвестиционная рекомендация")
    private String name;

    /**
     * Уникальный идентификатор рекомендации.
     */
    @Schema(description = "Уникальный идентификатор рекомендации", example = "recommendation123")
    private String id;

    /**
     * Текст рекомендации.
     */
    @Schema(description = "Текст рекомендации", example = "Рекомендуется рассмотреть инвестиции в...")
    private String text;

    /**
     * Конструктор для создания рекомендации.
     * @param name Название рекомендации
     * @param id Уникальный идентификатор рекомендации
     * @param text Текст рекомендации
     */
    public Recommendation(String name, String id, String text) {
        this.name = name;
        this.id = id;
        this.text = text;
    }

    /**
     * Возвращает название рекомендации.
     * @return Название рекомендации
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название рекомендации.
     * @param name Название рекомендации
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает уникальный идентификатор рекомендации.
     * @return Уникальный идентификатор рекомендации
     */
    public String getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор рекомендации.
     * @param id Уникальный идентификатор рекомендации
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Возвращает текст рекомендации.
     * @return Текст рекомендации
     */
    public String getText() {
        return text;
    }

    /**
     * Устанавливает текст рекомендации.
     * @param text Текст рекомендации
     */
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getId(),
                that.getId()) && Objects.equals(getText(), that.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getId(), getText());
    }
}
