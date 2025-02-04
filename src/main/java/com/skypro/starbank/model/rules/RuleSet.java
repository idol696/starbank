package com.skypro.starbank.model.rules;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Представляет набор правил для продукта в системе StarBank.
 */
@Entity
@Table(name = "rule_sets")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id","product_name","product_id","product_text","rules"})
@Schema(description = "Сущность набора правил, представляющая правила продукта")
public class RuleSet {

    /**
     * Уникальный идентификатор набора правил.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный ID набора правил", example = "1")
    private Long id;

    /**
     * Идентификатор продукта (UUID).
     */
    @Column(name = "product_id", nullable = false, unique = true, columnDefinition = "UUID")
    @Schema(description = "Идентификатор продукта (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @JsonProperty("product_id")
    private UUID productId;

    /**
     * Название продукта.
     */
    @Column(name = "product_name", nullable = false)
    @Schema(description = "Название продукта", example = "Invest 500")
    @JsonProperty("product_name")
    private String productName;

    /**
     * Описание продукта.
     */
    @Column(name = "product_text", columnDefinition = "TEXT")
    @Schema(description = "Описание продукта", example = "Детали инвестиции...")
    @JsonProperty("product_text")
    private String productText;

    /**
     * Список правил, входящих в данный набор.
     */
    @OneToMany(mappedBy = "ruleSet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Schema(description = "Список правил, входящих в данный набор")
    @JsonProperty("rules")
    private List<Rule> rules;

    public RuleSet() {}

    /**
     * Конструктор для создания набора правил.
     * @param productId идентификатор продукта
     * @param productName название продукта
     * @param productText описание продукта
     * @param rules список правил
     */
    public RuleSet(UUID productId, String productName, String productText, List<Rule> rules) {
        this.productId = productId;
        this.productName = productName;
        this.productText = productText;
        this.rules = rules;
    }

/**
 * Возвращает уникальный идентификатор набора правил.
 * @return идентификатор набора правил
 */
public Long getId() { return id; }

    /**
     * Возвращает идентификатор продукта.
     * @return идентификатор продукта
     */
    public UUID getProductId() { return productId; }

    /**
     * Устанавливает идентификатор продукта.
     * @param productId идентификатор продукта
     */
    public void setProductId(UUID productId) { this.productId = productId; }

    /**
     * Возвращает название продукта.
     * @return название продукта
     */
    public String getProductName() { return productName; }

    /**
     * Устанавливает название продукта.
     * @param productName название продукта
     */
    public void setProductName(String productName) { this.productName = productName; }

    /**
     * Возвращает описание продукта.
     * @return описание продукта
     */
    public String getProductText() { return productText; }

    /**
     * Устанавливает описание продукта.
     * @param productText описание продукта
     */
    public void setProductText(String productText) { this.productText = productText; }

    /**
     * Возвращает список правил, входящих в данный набор.
     * @return список правил
     */
    public List<Rule> getRules() { return rules; }

    /**
     * Устанавливает список правил, входящих в данный набор.
     * @param rules список правил
     */
    public void setRules(List<Rule> rules) { this.rules = rules; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleSet ruleSet = (RuleSet) o;
        return Objects.equals(id, ruleSet.id) && Objects.equals(productId, ruleSet.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId);
    }

    @Override
    public String toString() {
        return "RuleSet{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productText='" + productText + '\'' +
                ", rules=" + rules +
                '}';
    }
}
