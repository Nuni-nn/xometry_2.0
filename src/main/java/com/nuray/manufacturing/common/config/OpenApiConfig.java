package com.nuray.manufacturing.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI manufacturingQuoteOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Manufacturing Quote Platform API")
                        .version("v1")
                        .description("Backend API for custom manufacturing quotation requests."));
    }
}
