package com.skypro.starbank.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${project.version}")
    private String applicationVersion;

    /**
     * Настройка основной информации о документации API.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(applicationName + " API")
                        .version(applicationVersion)
                        .description("API для управления " + applicationName));
    }

    /**
     * Группировка для Rule Controller.
     */
    @Bean
    public GroupedOpenApi ruleApi() {
        return GroupedOpenApi.builder()
                .group("Rule Controller")
                .pathsToMatch("/rule/**")
                .build();
    }

    /**
     * Группировка для Recommendation Controller.
     */
    @Bean
    public GroupedOpenApi recommendationApi() {
        return GroupedOpenApi.builder()
                .group("Recommendation Controller")
                .pathsToMatch("/recommendation/**")
                .build();
    }

    /**
     * Группировка для Management Controller.
     */
    @Bean
    public GroupedOpenApi managementApi() {
        return GroupedOpenApi.builder()
                .group("Management Controller")
                .pathsToMatch("/management/**")
                .build();
    }
}