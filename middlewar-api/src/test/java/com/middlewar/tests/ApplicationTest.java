package com.middlewar.tests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Leboc Philippe.
 */
@SpringBootApplication(scanBasePackages = {"com.middlewar.api", "com.middlewar.core"})
@EnableAutoConfiguration
@EnableJpaRepositories("com.middlewar.api.dao")
@EntityScan({"com.middlewar.core"})
public class ApplicationTest {
    public static void main(String... args) {
        SpringApplication.run(ApplicationTest.class);
    }
}
