package com.skypro.starbank.configuration;

import com.skypro.starbank.configuration.conditions.OnMissingBuildInfoCondition;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.Properties;

@Configuration
@Conditional(OnMissingBuildInfoCondition.class) // Загружаем только если файла build-info.properties нет
public class BuildPropertiesConfig {

    @Bean
    @ConditionalOnMissingBean(BuildProperties.class)
    public BuildProperties buildProperties() {
        Properties properties = new Properties();

        System.err.println("⚠ Файл build-info.properties не найден, создаём бин с дефолтными значениями.");

        // Устанавливаем дефолтные значения
        properties.put("name", "Unknown Application");
        properties.put("version", "0.0.0");

        return new BuildProperties(properties);
    }
}
