package com.skypro.starbank.configuration;

import com.skypro.starbank.service.rulehandlers.RuleHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class RuleHandlerConfig {

    /**
     * Автоматически собирает все обработчики правил в Map.
     */
    @Bean
    public Map<String, RuleHandler> ruleHandlers(List<RuleHandler> handlers) {
        return handlers.stream()
                .collect(Collectors.toMap(RuleHandler::getRuleKey, Function.identity()));
    }
}
