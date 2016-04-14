package com;

import com.config.Config;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.data.xml.impl.ItemData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args)
    {
        String MODE = Config.DEV_CONFIG_DIRECTORY;

        if(args.length > 0){
            if(args[0].equalsIgnoreCase("prod")){
                MODE = Config.PROD_CONFIG_DIRECTORY;
            }
        }

        Config.load(MODE);
        BuildingData.getInstance();
        ItemData.getInstance();

        System.setProperty("spring.config.location", MODE+Config.APPLICATION_CONFIG_LOCATION);
        SpringApplication.run(Application.class, args);
    }
}
