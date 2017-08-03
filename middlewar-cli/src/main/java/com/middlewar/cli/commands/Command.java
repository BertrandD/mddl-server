package com.middlewar.cli.commands;

import lombok.Data;

/**
 * @author Bertrand
 */
@Data
public abstract class Command {
    private String[] input;
    private String usage;
    private String description;

    public abstract void exec();

    public Command(String usage, String description) {
        this.usage = usage;
        this.description = description;
    }

    public void printUsage() {
        System.out.println("usage : " + usage);
    }
}
