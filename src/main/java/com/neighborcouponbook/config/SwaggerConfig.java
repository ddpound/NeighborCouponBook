package com.neighborcouponbook.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc  // 추가
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("쿠폰북 API")
                .description("쿠폰북 API 문서")
                .version("v1.0.0");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
