package com.skypro.starbank.model.rules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RuleQueryType {
    USER_OF("USER_OF"),
    ACTIVE_USER_OF("ACTIVE_USER_OF"),
    TRANSACTION_SUM_COMPARE("TRANSACTION_SUM_COMPARE"),
    TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW");

    private final String value;

    RuleQueryType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RuleQueryType fromString(String value) {
        for (RuleQueryType type : RuleQueryType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Неизвестный тип запроса: " + value);
    }
}
