package com.middlewar.cli.commands;

import com.middlewar.cli.GameContext;
import com.middlewar.client.BaseClient;
import com.middlewar.dto.AccountDTO;
import com.middlewar.dto.BaseDTO;
import com.middlewar.dto.PlayerDTO;
import com.middlewar.dto.holder.BuildingHolderDTO;

import java.util.List;

/**
 * @author Bertrand
 */
public class InfoCommand extends Command {
    public InfoCommand() {
        super("info <account|player>", "Display info on the account");
    }

    @Override
    public void exec() {
        if (getInput().length <= 1) {
            printUsage();
            return;
        }


        switch (getInput()[1]) {
            case "account":
                AccountDTO account = GameContext.getInstance().getAccount();
                if (account == null) {
                    System.out.println("You are not logged in");
                    return;
                }

                System.out.println(account.toString());
                System.out.println("ID       : " + account.getId());
                System.out.println("Username : " + account.getUsername());
                break;
            case "player":
                PlayerDTO player = GameContext.getInstance().getPlayer();
                if (player == null) {
                    System.out.println("You don't have any player. Let's create it !");
                    return;
                }
                System.out.println(player.toString());
                System.out.println("ID   : " + player.getId());
                System.out.println("Name : " + player.getName());
                break;
            case "base":
                BaseDTO base = GameContext.getInstance().getBase();
                if (base == null) {
                    System.out.println("You don't have any base. Let's create it !");
                    return;
                }
                System.out.println(base.toString());
                System.out.println("ID   : " + base.getId());
                System.out.println("Name : " + base.getName());
                break;
            case "base.buildable":
                List<BuildingHolderDTO> buildings = BaseClient.getBuildables(GameContext.getInstance().getBase());
                buildings.forEach(k-> System.out.println(k.getTemplateId()));
                break;
        }

    }
}
