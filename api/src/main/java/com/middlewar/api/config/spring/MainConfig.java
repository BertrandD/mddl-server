package com.middlewar.api.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author LEBOC Philippe
 */
@Configuration
@EnableScheduling
@EnableAsync
@Import(value = {
    WebConfig.class,
    MongoConfig.class,
    SecurityConfig.class,
    SwaggerConfig.class
})
public class MainConfig {}
