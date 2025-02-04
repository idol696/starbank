package com.skypro.starbank.model.rules;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Обертка для списка наборов правил (RuleSet), используемая для корректного форматирования JSON ответа.
 */
@Schema(description = "Обертка для списка наборов правил")
public class RuleSetWrapper {

    /**
     * Список наборов правил.
     */
    @JsonProperty("data")
    @Schema(description = "Список наборов правил")
    private List<RuleSet> data;

    /**
     * Конструктор для создания обертки набора правил.
     * @param data список наборов правил
     */
    public RuleSetWrapper(List<RuleSet> data) {
        this.data = data;
    }

    /**
     * Возвращает список наборов правил.
     * @return список наборов правил
     */
    public List<RuleSet> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "RuleSetWrapper{" +
                "rules=" + data +
                '}';
    }
}

