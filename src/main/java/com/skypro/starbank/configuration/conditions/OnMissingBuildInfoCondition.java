package com.skypro.starbank.configuration.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnMissingBuildInfoCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // Проверяем, существует ли файл в classpath
        return !new ClassPathResource("META-INF/build-info.properties").exists();
    }
}
