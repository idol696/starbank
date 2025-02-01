package com.skypro.starbank.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 • Представляет информацию о продукте.
 */
@Schema(description = "Информация о продукте")
public class Product {

    /**
     * Уникальный идентификатор продукта.
     */
    @Schema(description = "Уникальный идентификатор продукта", example = "product123")
    private String id;

    /**
     * Название продукта.
     */
    @Schema(description = "Название продукта", example = "Инвестиционный продукт")
    private String name;

    /**
     * Тип продукта.
     */
    @Schema(description = "Тип продукта", example = "Инвестиции")
    private String type;

    /**
     * Описание продукта.
     */
    @Schema(description = "Описание продукта", example = "Подробное описание инвестиционного продукта...")
    private String description;

    public Product() {}

    /**
     * Конструктор для создания продукта.
     * @param id уникальный идентификатор продукта
     * @param name название продукта
     * @param type тип продукта
     * @param description описание продукта
     */
    public Product(String id, String name, String type, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    /**
     * Возвращает уникальный идентификатор продукта.
     * @return уникальный идентификатор продукта
     */
    public String getId() { return id; }

    /**
     * Возвращает название продукта.
     * @return название продукта
     */
    public String getName() { return name; }

    /**
     * Возвращает тип продукта.
     * @return тип продукта
     */
    public String getType() { return type; }

    /**
     * Возвращает описание продукта.
     * @return описание продукта
     */
    public String getDescription() { return description; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(getId(), product.getId()) && Objects.equals(getName(), product.getName())
                && Objects.equals(getType(), product.getType()) && Objects.equals(getDescription(), product.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getType(), getDescription());
    }
}
