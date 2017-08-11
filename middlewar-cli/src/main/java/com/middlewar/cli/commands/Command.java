package com.middlewar.cli.commands;

import com.middlewar.cli.CommandHandler;
import lombok.Data;

/**
 * @author Bertrand
 */
@Data
public abstract class Command {
    private String[] input;
    private String usage;
    private String description;

    public Command(String usage, String description) {
        this.usage = usage;
        this.description = description;
    }

    public abstract void exec();

    protected String getParam(String name, int pos) {
        if (getInput().length > pos) {
            System.out.println(name + " : " + getInput()[pos]);
            return getInput()[pos];
        } else {
            System.out.print(name + " : ");
            return CommandHandler.askForString();
        }

    }

    public void printUsage() {
        System.out.println("usage : " + usage);
    }
}
