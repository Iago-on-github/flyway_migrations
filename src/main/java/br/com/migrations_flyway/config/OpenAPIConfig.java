package br.com.migrations_flyway.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(("RESTful API with Java and Spring Boot"))
                        .version("v1")
                        .description("Some description about your API")
                        .termsOfService("http://localhost:8080/termsOfService")
                        .license(new License().name("Apache 2.0")
                                .url("http://localhost:8080/teste")));
    }
}
