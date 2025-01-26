package com.skypro.starbank.model.rules;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

/**
 * Представляет набор правил для определенного продукта.
 * <p>
 * Набор правил включает в себя идентификатор продукта, имя, описание
 * и список правил, которые применяются к продукту.
 * </p>
 */
@Entity
@Table(name = "rule_sets")
public class RuleSet {

    /**
     * Уникальный идентификатор набора правил.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор продукта, для которого предназначен данный набор правил.
     */
    @Column(name = "product_id")
    private String productId;

    /**
     * Имя набора правил.
     */
    @Column(name = "name")
    private String name;

    /**
     * Описание набора правил.
     */
    @Column(name = "description")
    private String description;

    /**
     * Список правил, входящих в данный набор правил.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_set_id")
    private List<Rule> conditions;


    /**
     * Конструктор без аргументов, необходимый для JPA.
     */
    public RuleSet() {
    }

    /**
     * Конструктор для создания набора правил с указанными параметрами.
     *
     * @param productId   Идентификатор продукта.
     * @param name        Имя набора правил.
     * @param description Описание набора правил.
     * @param conditions  Список правил.
     */
    public RuleSet(String productId, String name, String description, List<Rule> conditions) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.conditions = conditions;
    }

    /**
     * Возвращает уникальный идентификатор набора правил.
     *
     * @return Идентификатор набора правил.
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор набора правил.
     *
     * @param id Идентификатор набора правил.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Возвращает идентификатор продукта, для которого предназначен данный набор правил.
     *
     * @return Идентификатор продукта.
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Устанавливает идентификатор продукта.
     *
     * @param productId Идентификатор продукта.
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Возвращает имя набора правил.
     *
     * @return Имя набора правил.
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает имя набора правил.
     *
     * @param name Имя набора правил.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает описание набора правил.
     *
     * @return Описание набора правил.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание набора правил.
     *
     * @param description Описание набора правил.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Возвращает список правил, входящих в данный набор правил.
     *
     * @return Список правил.
     */
    public List<Rule> getConditions() {
        return conditions;
    }

    /**
     * Устанавливает список правил.
     *
     * @param conditions Список правил.
     */
    public void setConditions(List<Rule> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleSet ruleSet = (RuleSet) o;
        return Objects.equals(id, ruleSet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "RuleSet{" +
                "id=" + id +
                ", productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", conditions=" + conditions +
                '}';
    }
}
