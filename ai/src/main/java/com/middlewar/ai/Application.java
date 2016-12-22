package com.middlewar.ai;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private static final Logger _log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        AI john = new AI();

        john.run();
    }
}
