package com.middlewar.boot.config;

import com.middlewar.api.annotations.spring.profiles.Dev;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author LEBOC Philippe
 */
@Dev
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${middlewar.api.version}")
    private String version;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.middlewar"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Middlewar API documentation",
                "This documentation provide informations about requests to the server API.",
                version,
                "#",
                new Contact("Middlewar Team", "#", ""),
                "", "#"
        );
    }
}