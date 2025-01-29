package com.angelfg.app.configs;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Transferencias",
        version = "1.0.0",
        description = "Documentation for endpoints in Transferencias App"
    )
)
public class OpenApiConfig {
    // Endpoints Swagger
    // URL: http://localhost:3030/swagger-ui/index.html#/
}
