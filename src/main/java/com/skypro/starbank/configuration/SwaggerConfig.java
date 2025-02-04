package com.skypro.starbank.configuration;

import com.skypro.starbank.service.ManagementService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public final  ManagementService managementService;

    public SwaggerConfig(ManagementService managementService) {
        this.managementService = managementService;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(managementService.getServiceInfo().get("name"))
                        .version(managementService.getServiceInfo().get("version"))
                        .description("API для управления StarBank Recommended Program for SkyPro School"));
    }

    @Bean
    public GroupedOpenApi ruleApi() {
        return GroupedOpenApi.builder()
                .group("Rule Controller")
                .pathsToMatch("/rule/**")
                .build();
    }

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