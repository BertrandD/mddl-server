package com.handler;

import java.util.logging.Logger;

/**
 * @author LEBOC Philippe
 */
public interface ICommandHandler {

    Logger _log = Logger.getLogger(ICommandHandler.class.getName());

    /**
     * this is the worker method that is called when someone uses a command.
     * @param command
     * @param params
     * @return command success
     */
    boolean useCommand(String command, String params);

    /**
     * this method is called at initialization to register all the item ids automatically
     * @return all known itemIds
     */
    String[] getCommandList();
}
