package com;

import com.config.Config;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.data.xml.impl.SystemMessageData;
import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableJSONDoc
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

        // Config
        Config.load(MODE);

        // Parse
        SystemMessageData.getInstance();
        ItemData.getInstance();
        BuildingData.getInstance();

        // Spring
        System.setProperty("spring.config.location", MODE+Config.APPLICATION_CONFIG_LOCATION);
        SpringApplication.run(Application.class, args);

        // Console
        //final Scanner scanner = new Scanner(System.in);
        //Thread console = new Thread(new ConsoleImpl(scanner));
        //console.start();
    }
}