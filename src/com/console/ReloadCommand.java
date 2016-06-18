package com.console;

import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.data.xml.impl.SystemMessageData;
import com.handler.CommandHandler;
import com.handler.ICommandHandler;
import com.util.Utils;

/**
 * @author LEBOC Philippe
 */
public class ReloadCommand implements ICommandHandler {

    private static final String[] COMMANDS =
    {
        "reload"
    };

    @Override
    public boolean useCommand(String command, String params) {
        if(command.startsWith("reload"))
        {
            if(params == null){
                Utils.println("Failed !\r\nUsage: reload <item|building|msg>");
                return true;
            }

            // TODO: be sur is one param
            switch (params.toLowerCase())
            {
                case "item":
                case "items":
                    ItemData.getInstance().load();
                    Utils.println("Items reloaded successfully !");
                    break;
                case "building":
                case "buildings":
                    BuildingData.getInstance().load();
                    Utils.println("Buildings reloaded successfully !");
                    break;
                case "msg":
                case "sysmsg":
                    SystemMessageData.getInstance().load();
                    Utils.println("System messages reloaded successfully !");
                    break;
                default:
                    Utils.println("Failed !\r\nUsage: reload <handler|item|building|msg>");
            }
            return true;
        }
        return false;
    }

    @Override
    public String[] getCommandList() {
        return COMMANDS;
    }
}
