package com.middlewar.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AI {
    private static final Logger _log = LoggerFactory.getLogger(AI.class);

    private boolean isRunning;

    void run() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        _log.info("Starting...");

        while (isRunning) {
            _log.info("Waiting...");
        }
    }
}
