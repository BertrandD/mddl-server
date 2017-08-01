package com.middlewar.tests;

import com.middlewar.core.config.Config;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.data.xml.SystemMessageData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.PostConstruct;

/**
 * @author Leboc Philippe.
 */
@SpringBootApplication(scanBasePackages = {"com.middlewar.api", "com.middlewar.core"})
@EnableAutoConfiguration
@EnableJpaRepositories("com.middlewar.api.dao")
@EntityScan({"com.middlewar.core"})
@ContextConfiguration(locations={"classpath:application.properties"})
public class ApplicationTest {
    public static void main(String... args) {
        SpringApplication.run(ApplicationTest.class);
    }

    @PostConstruct
    public void init() {

        // Override default config file location
        Config.APPLICATION_CONFIG_LOCATION = "./src/test/resources/config/";
        Config.GENERAL_CONFIG_FILE = "./src/test/resources/general.properties";
        Config.UNIVERS_CONFIG_FILE = "./src/test/resources/univers.properties";

        // Loading config
        Config.load();

        // Override default "DATA" directory
        Config.DATA_ROOT_DIRECTORY = "./src/test/resources/data/";

        BuildingData.getInstance();
        SystemMessageData.getInstance();
        ItemData.getInstance();
    }
}
