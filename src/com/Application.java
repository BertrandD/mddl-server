package com;

import com.config.Config;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.data.xml.impl.ShopData;
import com.gameserver.data.xml.impl.SystemMessageData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class Application {

    public static void main(String[] args)
    {
        String MODE = Config.DEV_CONFIG_DIRECTORY;

        if(args.length > 0){
            if(args[0].equalsIgnoreCase("prod")){
                MODE = Config.PROD_CONFIG_DIRECTORY;
            }
        }

        // Config
        Config.load(MODE);

        // Parse
        SystemMessageData.getInstance();
        ItemData.getInstance();
        BuildingData.getInstance();
        ShopData.getInstance();

        // Spring
        System.setProperty("spring.config.location", MODE+Config.APPLICATION_CONFIG_LOCATION);
        SpringApplication.run(Application.class, args);
    }
}