package com.handler;

import com.console.BuildingCommand;
import com.console.HelpCommand;
import com.console.ItemCommand;
import com.console.ReloadCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author LEBOC Philippe
 */
public class CommandHandler implements IHandler<ICommandHandler, String>
{
    private final Map<String, ICommandHandler> _datatable;

    protected CommandHandler()
    {
        _datatable = new HashMap<>();
        load();
    }

    public synchronized void load() {
        _datatable.clear();

        // Register
        registerHandler(new HelpCommand());
        registerHandler(new ReloadCommand());
        registerHandler(new ItemCommand());
        registerHandler(new BuildingCommand());
    }

    public Set<String> getAllCommand() {
        return _datatable.keySet();
    }

    @Override
    public void registerHandler(ICommandHandler handler)
    {
        for (String id : handler.getCommandList())
        {
            _datatable.put(id, handler);
        }
    }

    @Override
    public synchronized void removeHandler(ICommandHandler handler)
    {
        for (String id : handler.getCommandList())
        {
            _datatable.remove(id);
        }
    }

    @Override
    public ICommandHandler getHandler(String command)
    {
        return _datatable.get(command.contains(" ") ? command.substring(0, command.indexOf(" ")) : command);
    }

    @Override
    public int size()
    {
        return _datatable.size();
    }

    public static CommandHandler getInstance()
    {
        return SingletonHolder._instance;
    }

    private static class SingletonHolder
    {
        protected static final CommandHandler _instance = new CommandHandler();
    }
}