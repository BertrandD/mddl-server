package com.console;

import com.handler.CommandHandler;
import com.handler.ICommandHandler;
import com.util.Utils;

/**
 * @author LEBOC Philippe
 */
public class HelpCommand implements ICommandHandler
{
    private static final String[] COMMANDS =
    {
        "help"
    };

    @Override
    public boolean useCommand(String command, String params)
    {
        if(command.equalsIgnoreCase("help"))
        {
            Utils.println("Command list :");
            for(String cmd : CommandHandler.getInstance().getAllCommand()) {
                Utils.println("\t"+cmd);
            }
            return true;
        }
        return false;
    }

    @Override
    public String[] getCommandList()
    {
        return COMMANDS;
    }
}