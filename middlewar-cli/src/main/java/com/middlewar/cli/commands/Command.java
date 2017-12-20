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
        return getParam(name, pos, false);
    }

    protected String getParam(String name, int pos, boolean password) {
        if (getInput().length > pos) {
            System.out.println(name + " : " + (password ? "****" : getInput()[pos]));
            return getInput()[pos];
        } else {
            return CommandHandler.askForString(name + " : ", password);
        }

    }

    public void printUsage() {
        System.out.println("usage : " + usage);
    }
}
