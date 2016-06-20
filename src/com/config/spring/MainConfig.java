package com.config.spring;

import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan(basePackages = "com")
@Import(value = {
    WebConfig.class,
    SecurityConfig.class
})
public class MainConfig {
}
