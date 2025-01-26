
package com.skypro.starbank.model.rules;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Представляет правило, которое применяется к продукту.
 * <p>
 *     Правило может определять условия, которым должен соответствовать продукт,
 *     например, его тип, значение и сравнение с другими значениями.
 *     Правила могут быть вложенными, образуя сложные условия.
 * </p>
 */
@Entity
@Table(name = "rules")
public class Rule {

    /**
     * Уникальный идентификатор правила.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Тип правила (например, "PRODUCT_TYPE", "AMOUNT").
     */
    @Column(name = "type")
    private String type;

    /**
     * Тип продукта, к которому применяется данное правило.
     */
    @Column(name = "product_type")
    private String productType;

    /**
     * Флаг, указывающий на необходимость инвертирования результата правила.
     */
    @Column(name = "negate")
    private boolean negate;

    /**
     * Оператор сравнения (например, "=", ">", "<").
     */
    @Column(name = "operator")
    private String operator;

    /**
     * Значение для сравнения.
     */
    @Column(name = "value")
    private Double value;

    /**
     * Значение, с которым нужно сравнить.
     */
    @Column(name = "compare_to")
    private String compareTo;

    /**
     * Тип сравнения (например, "STRING", "NUMBER").
     */
    @Column(name = "compare_type")
    private String compareType;

    /**
     *  Используется для сохранения вложенных правил.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_rule_id")
    private List<Rule> conditions;


    /**
     * Конструктор без аргументов, необходимый для JPA.
     */
    public Rule() {}

    /**
     * Возвращает уникальный идентификатор правила.
     *
     * @return уникальный идентификатор правила.
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор правила.
     * @param id уникальный идентификатор правила.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Возвращает тип правила.
     *
     * @return Тип правила.
     */
    public String getType() {
        return type;
    }

    /**
     * Устанавливает тип правила.
     *
     * @param type Тип правила.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Возвращает тип продукта, к которому применяется данное правило.
     *
     * @return Тип продукта.
     */
    public String getProductType() {
        return productType;
    }

    /**
     * Устанавливает тип продукта.
     *
     * @param productType Тип продукта.
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * Возвращает флаг инвертирования результата правила.
     *
     * @return True, если результат правила должен быть инвертирован, иначе false.
     */
    public boolean isNegate() {
        return negate;
    }

    /**
     * Устанавливает флаг инвертирования результата правила.
     *
     * @param negate Флаг инвертирования.
     */
    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    /**
     * Возвращает оператор сравнения.
     *
     * @return Оператор сравнения.
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Устанавливает оператор сравнения.
     *
     * @param operator Оператор сравнения.
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * Возвращает значение для сравнения.
     *
     * @return Значение для сравнения.
     */
    public Double getValue() {
        return value;
    }

    /**
     * Устанавливает значение для сравнения.
     *
     * @param value Значение для сравнения.
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Возвращает значение, с которым нужно сравнить.
     *
     * @return Значение для сравнения.
     */
    public String getCompareTo() {
        return compareTo;
    }

    /**
     * Устанавливает значение, с которым нужно сравнить.
     *
     * @param compareTo Значение для сравнения.
     */
    public void setCompareTo(String compareTo) {
        this.compareTo = compareTo;
    }

    /**
     * Возвращает тип сравнения.
     *
     * @return Тип сравнения.
     */
    public String getCompareType() {
        return compareType;
    }

    /**
     * Устанавливает тип сравнения.
     *
     * @param compareType Тип сравнения.
     */
    public void setCompareType(String compareType) {
        this.compareType = compareType;
    }

    /**
     * Возвращает список вложенных правил.
     * @return список вложенных правил.
     */
    public List<Rule> getConditions() {
        return conditions;
    }

    /**
     * Устанавливает список вложенных правил.
     * @param conditions список вложенных правил.
     */
    public void setConditions(List<Rule> conditions) {
        this.conditions = conditions;
    }

    /**
     * Проверяет, есть ли у правила вложенные правила.
     *
     * @return True, если есть вложенные правила, иначе false.
     */
    public boolean hasSubRules() {
        return conditions != null && !conditions.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(id, rule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", productType='" + productType + '\'' +
                ", negate=" + negate +
                ", operator='" + operator + '\'' +
                ", value=" + value +
                ", compareTo='" + compareTo + '\'' +
                ", compareType='" + compareType + '\'' +
                ", conditions=" + conditions +
                '}';
    }
}
