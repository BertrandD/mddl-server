package com.middlewar.cli.commands;

import com.middlewar.cli.CommandHandler;

/**
 * @author Bertrand
 */
public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "display help");
    }

    @Override
    public void exec() {
        System.out.println("Commands available : ");
        CommandHandler.COMMANDS.forEach((k, v) -> System.out.println(" - " + v.getUsage() + " : " + v.getDescription()));
    }
}
