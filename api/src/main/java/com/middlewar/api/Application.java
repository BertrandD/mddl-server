package com.middlewar.api;

import com.middlewar.api.services.impl.AstralObjectServiceImpl;
import com.middlewar.core.config.Config;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.data.xml.ShopData;
import com.middlewar.core.data.xml.SystemMessageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories("com.middlewar.api.dao")
@EntityScan({"com.middlewar.core.model", "com.middlewar.core.holders"})
@ComponentScan("com")
public class Application extends AsyncConfigurerSupport implements CommandLineRunner{

    @Autowired
    private AI ai;

    @Autowired
    private AstralObjectServiceImpl astralObjectServiceImpl;

    public static void main(String[] args)
    {
        // Config
        Config.load();

        // Parsing JSON
        WorldData.getInstance();

        // Parsing XML
        SystemMessageData.getInstance();
        ItemData.getInstance();
        BuildingData.getInstance();
        ShopData.getInstance();

        // Spring
        System.setProperty("spring.config.location", Config.APPLICATION_CONFIG_LOCATION);
        SpringApplication.run(Application.class, args);
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("MiddleWar-");
        executor.initialize();
        return executor;
    }

    @Override
    public void run(String... strings) throws Exception {
        astralObjectServiceImpl.saveUniverse();
        ai.init();
   }
}