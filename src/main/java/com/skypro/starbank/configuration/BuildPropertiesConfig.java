package com.skypro.starbank.configuration;

import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.Properties;

@Configuration
public class BuildPropertiesConfig {

    @Bean
    @ConditionalOnMissingBean(BuildProperties.class)
    public BuildProperties fallbackBuildProperties() {
        Properties properties = new Properties();
        properties.put("name", "Unknown Application");
        properties.put("version", "0.0.0");

        return new BuildProperties(properties);
    }
}
