package com.skypro.starbank.model.rules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.starbank.exception.RulesArgumentIndexNotFoundException;
import com.skypro.starbank.exception.RulesArgumentSerializationException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

/**
 * Представляет правило в системе StarBank.
 */
@Entity
@Table(name = "rules")
@JsonPropertyOrder({ "query", "arguments", "negate" })
@Schema(description = "Представляет правило в системе StarBank")
public class Rule {

    /**
     * Уникальный идентификатор правила.
     */
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    /**
     * Набор правил, к которому принадлежит данное правило.
     */
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_set_id")
    @Schema(hidden = true)
    private RuleSet ruleSet;


    /**
     * Запрос, связанный с правилом.
     */
    @Column(name = "query", nullable = false)
    @Schema(description = "Запрос, связанный с правилом", required = true)
    private String query;

    /**
     * Аргументы для правила в формате JSON.
     */
    @Column(name = "arguments", columnDefinition = "TEXT")
    @Schema(description = "Список аргументов для правила", required = false)
    private String argumentsJson;

    /**
     * Флаг, указывающий, нужно ли инвертировать результат правила.
     */
    @Column(name = "negate", nullable = false)
    @Schema(description = "Флаг, указывающий, нужно ли инвертировать результат правила", required = true)
    private boolean negate;


    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Rule() {}

    /**
     * Конструктор для создания правила.
     * @param query запрос правила
     * @param arguments список аргументов правила
     * @param negate флаг инверсии результата
     */
    public Rule(String query, List<String> arguments, boolean negate) {
        this.query = query;
        this.negate = negate;
        setArguments(arguments);
    }

    /**
     * Возвращает уникальный идентификатор правила.
     * @return идентификатор правила
     */
    public Long getId() { return id; }

    /**
     * Устанавливает уникальный идентификатор правила.
     * @param id идентификатор правила
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Возвращает запрос, связанный с правилом.
     * @return запрос правила
     */
    public String getQuery() { return query; }

    /**
     * Устанавливает запрос, связанный с правилом.
     * @param query запрос правила
     */
    public void setQuery(String query) { this.query = query; }

    /**
     * Возвращает флаг, указывающий, нужно ли инвертировать результат правила.
     * @return флаг инверсии
     */
    public boolean isNegate() { return negate; }

    /**
     * Устанавливает флаг, указывающий, нужно ли инвертировать результат правила.
     * @param negate флаг инверсии
     */
    public void setNegate(boolean negate) { this.negate = negate; }

    /**
     * Возвращает аргумент по индексу.
     * @param index индекс аргумента
     * @return аргумент по индексу
     * @throws RulesArgumentIndexNotFoundException если аргумент не найден
     */
    public String getArgument(int index) {
        if (getArguments().get(index).isEmpty()) {
            throw new RulesArgumentIndexNotFoundException();
        }
        return getArguments().get(index);
    }

    /**
     * Возвращает список аргументов правила.
     * @return список аргументов правила
     * @throws RulesArgumentSerializationException если ошибка десериализации
     */
    @Schema(hidden = true)
    public List<String> getArguments() {
        try {
            return objectMapper.readValue(argumentsJson, List.class);
        } catch (JsonProcessingException e) {
            throw new RulesArgumentSerializationException("Ошибка десериализации arguments: " + argumentsJson);
        }
    }

    /**
     * Устанавливает список аргументов правила.
     * @param arguments список аргументов
     * @throws RulesArgumentSerializationException если ошибка сериализации
     */
    public void setArguments(List<String> arguments) {
        try {
            this.argumentsJson = objectMapper.writeValueAsString(arguments);
        } catch (JsonProcessingException e) {
            throw new RulesArgumentSerializationException("Ошибка сериализации arguments: " + arguments);
        }
    }

    /**
     * Возвращает набор правил, к которому принадлежит данное правило.
     * @return набор правил
     */
    @Schema(hidden = true)
    public RuleSet getRuleSet() { return ruleSet; }

    /**
     * Устанавливает набор правил, к которому принадлежит данное правило.
     * @param ruleSet набор правил
     */
    @Schema(hidden = true)
    public void setRuleSet(RuleSet ruleSet) { this.ruleSet = ruleSet; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return isNegate() == rule.isNegate() && Objects.equals(getId(), rule.getId()) && Objects.equals(getRuleSet(),
                rule.getRuleSet()) && Objects.equals(getQuery(), rule.getQuery()) &&
                Objects.equals(argumentsJson, rule.argumentsJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRuleSet(), getQuery(), argumentsJson, isNegate());
    }

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", query='" + query + '\'' +
                ", arguments=" + argumentsJson +
                ", negate=" + negate +
                '}';
    }
}
