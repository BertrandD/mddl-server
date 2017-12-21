package com.middlewar.cli.commands;

import java.util.concurrent.TimeUnit;

/**
 * @author Bertrand
 */
public class WaitCommand extends Command {
    public WaitCommand() {
        super("wait <milliseconds>", "Wait the given time");
    }

    @Override
    public void exec() {
        String waitTime = getParam("Milliseconds", 1);

        try {
            TimeUnit.MILLISECONDS.sleep(Long.parseLong(waitTime, 10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
