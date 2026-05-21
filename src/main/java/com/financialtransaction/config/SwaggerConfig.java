package com.financialtransaction.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI financialTransactionOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Financial Transaction Processing API")
                        .description("Production Grade Financial Transaction Processing Service APIs")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Venkata Nikhil")
                                .email("nikhil@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("https://example.com/docs"));
    }
}