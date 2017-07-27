package com.middlewar.api.tasks;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    //@Scheduled(fixedRate = 60000)
    public void reportCurrentTime() {
        System.out.println("Time is : " + dateFormat.format(new Date()));
    }
}
