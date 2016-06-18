package com.console;

import com.gameserver.data.xml.impl.ItemData;
import com.handler.ICommandHandler;
import com.util.Utils;

/**
 * @author LEBOC Philippe
 */
public class ItemCommand implements ICommandHandler {

    private static String[] COMMANDS =
    {
        "item"
    };

    @Override
    public boolean useCommand(String command, String params) {

        if(command.equalsIgnoreCase("item"))
        {
            if(params == null)
            {
                showInfo();
            }
            else
            {
                // TODO: handle with args
            }
            return true;
        }
        return false;
    }

    private void showInfo()
    {
        Utils.println("Items Informations:");
        final ItemData data = ItemData.getInstance();

        Utils.println("\tCargos\t: "+data.getCargos().size());
        Utils.println("\tEngines\t: " + data.getEngines().size());
        Utils.println("\tModules\t: " + data.getModules().size());
        Utils.println("\tResources\t: " + data.getResources().size());
        Utils.println("\tStructures\t: " + data.getStructures().size());
        Utils.println("\tWeapons\t: " + data.getWeapons().size());
        Utils.println("\tCommon Items\t: " + data.getCommonItems().size());
        Utils.println("\tTotal\t: "+data.getSize());
    }

    @Override
    public String[] getCommandList() {
        return COMMANDS;
    }
}
