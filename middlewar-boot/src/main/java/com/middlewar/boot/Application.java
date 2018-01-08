package com.middlewar.boot;

import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.data.xml.ShopData;
import com.middlewar.core.data.xml.SystemMessageData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com")
@EntityScan(basePackages = "com.middlewar.core.model")
@EnableJpaRepositories(basePackages = "com.middlewar.core.repository")
public class Application extends AsyncConfigurerSupport {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init() {
        SystemMessageData.getInstance();
        ItemData.getInstance();
        BuildingData.getInstance();
        ShopData.getInstance();
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
