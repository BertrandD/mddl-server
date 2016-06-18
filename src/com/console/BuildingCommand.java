package com.console;

import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.model.buildings.Building;
import com.handler.ICommandHandler;
import com.util.Utils;

/**
 * @author LEBOC Philippe
 */
public class BuildingCommand implements ICommandHandler {

    private static String[] COMMANDS =
    {
        "building"
    };

    @Override
    public boolean useCommand(String command, String params) {
        if(command.equalsIgnoreCase("building"))
        {
            final BuildingData data = BuildingData.getInstance();

            Utils.println("Buildings Informations:");

            for (Building building : data.getBuildings()) {
                Utils.println("\t[Max Lv"+building.getMaxLevel()+"]\t "+building.getName());
            }

            Utils.println("\tTotal count\t: " + data.getBuildings().size());

            return true;
        }
        return false;
    }

    @Override
    public String[] getCommandList() {
        return COMMANDS;
    }
}
