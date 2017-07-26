package com.middlewar.tests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Leboc Philippe.
 */
@SpringBootApplication(scanBasePackages = {"com.middlewar.api", "com.middlewar.core"})
@EnableAutoConfiguration
public class ApplicationTest {
    public static void main(String... args) {
        SpringApplication.run(ApplicationTest.class);
    }
}
