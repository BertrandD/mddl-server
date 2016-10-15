package com;

import com.config.Config;
import com.gameserver.data.xml.BuildingData;
import com.gameserver.data.xml.ItemData;
import com.gameserver.data.xml.ShopData;
import com.gameserver.data.xml.SystemMessageData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com")
public class Application extends AsyncConfigurerSupport {

    public static void main(String[] args)
    {
        // Config
        Config.load();

        // Parse
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
}