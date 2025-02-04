package com.skypro.starbank.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;
import java.util.Objects;

/**
 • Представляет транзакцию в системе StarBank.
 */
@Schema(description = "Информация о транзакции")
public class Transaction {

    /**
     * Уникальный идентификатор транзакции.
     */
    @Schema(description = "Уникальный идентификатор транзакции", example = "transaction123")
    private final String id;

    /**
     * Пользователь, совершивший транзакцию.
     */
    @Schema(description = "Пользователь, совершивший транзакцию")
    private final User user;

    /**
     * Продукт, связанный с транзакцией.
     */
    @Schema(description = "Продукт, связанный с транзакцией")
    private final Product product;

    /**
     * Сумма транзакции.
     */
    @Schema(description = "Сумма транзакции", example = "100.00")
    private final double amount;

    /**
     * Тип транзакции.
     */
    @Schema(description = "Тип транзакции", example = "покупка")
    private final String type;

    /**
     * Дата и время транзакции.
     */
    @Schema(description = "Дата и время транзакции", example = "2024-07-26T10:00:00.000Z")
    private final Timestamp date;

    /**
     * Конструктор для создания транзакции.
     * @param id Уникальный идентификатор транзакции
     * @param user Пользователь, совершивший транзакцию
     * @param product Продукт, связанный с транзакцией
     * @param amount Сумма транзакции
     * @param type Тип транзакции
     * @param date Дата и время транзакции
     */
    public Transaction(String id, User user, Product product, double amount, String type, Timestamp date) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    /**
     * Возвращает уникальный идентификатор транзакции.
     * @return Уникальный идентификатор транзакции
     */
    public String getId() { return id; }

    /**
     * Возвращает пользователя, совершившего транзакцию.
     * @return Пользователь, совершивший транзакцию
     */
    public User getUser() { return user; }

    /**
     * Возвращает продукт, связанный с транзакцией.
     * @return Продукт, связанный с транзакцией
     */
    public Product getProduct() { return product; }

    /**
     * Возвращает сумму транзакции.
     * @return Сумма транзакции
     */
    public double getAmount() { return amount; }

    /**
     * Возвращает тип транзакции.
     * @return Тип транзакции
     */
    public String getType() { return type; }

    /**
     * Возвращает дату и время транзакции.
     * @return Дата и время транзакции
     */
    public Timestamp getDate() { return date; }

    /**
     * Возвращает идентификатор пользователя, совершившего транзакцию.
     * @return Идентификатор пользователя
     */
    @Schema(hidden = true)
    public String getUserId() { return user.getId(); }

    /**
     * Возвращает идентификатор продукта, связанного с транзакцией.
     * @return Идентификатор продукта
     */
    @Schema(hidden = true)
    public String getProductId() { return product.getId(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(getAmount(), that.getAmount()) == 0 && Objects.equals(getId(),
                that.getId()) && Objects.equals(getUser(), that.getUser()) && Objects.equals(getProduct(),
                that.getProduct()) && Objects.equals(getType(), that.getType()) && Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getProduct(), getAmount(), getType(), getDate());
    }
}