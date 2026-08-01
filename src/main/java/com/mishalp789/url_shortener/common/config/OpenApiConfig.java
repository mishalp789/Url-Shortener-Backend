package com.mishalp789.url_shortener.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI urlShortenerAPI(){
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("URL Shortener API")
                        .description("Production-ready URL Shortener Backend built with Spring Boot")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Mishal")
                                .email("mishalp789@gmail.com")
                                .url("https://github.com/mishalp789"))
                        .license(new License()
                                .name("mishalp789")))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .schemaRequirement(
                        securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"));
    }
}
