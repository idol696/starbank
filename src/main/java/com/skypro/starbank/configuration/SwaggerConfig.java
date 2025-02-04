package com.skypro.starbank.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String BUILD_DESC = "API для управления StarBank Recommended Program for SkyPro School";

    private final BuildProperties buildProperties;

    @Autowired
    public SwaggerConfig(@Autowired(required = false) BuildProperties buildProperties) {
        this.buildProperties = (buildProperties != null) ? buildProperties : getDefaultBuildProperties();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(buildProperties.getName())
                        .version(buildProperties.getVersion())
                        .description(BUILD_DESC));
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

    @Bean
    public GroupedOpenApi managementApi() {
        return GroupedOpenApi.builder()
                .group("Management Controller")
                .pathsToMatch("/management/**")
                .build();
    }

    private BuildProperties getDefaultBuildProperties() {
        return new BuildProperties(new java.util.Properties() {{
            put("name", "Unknown Application");
            put("version", "0.0.0");
        }});
    }
}
