package com.middlewar.cli.commands;

import com.middlewar.cli.GameContext;
import com.middlewar.client.BaseClient;
import com.middlewar.client.PlayerClient;
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
        super("info <account|player|base>", "Display info on the account");
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
                GameContext.getInstance().setPlayer(PlayerClient.getPlayer(GameContext.getInstance().getPlayer().getId()));
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
                GameContext.getInstance().setBase(BaseClient.getBase(GameContext.getInstance().getBase().getId()));
                BaseDTO base = GameContext.getInstance().getBase();
                if (base == null) {
                    System.out.println("You don't have any base. Let's create it !");
                    return;
                }
                System.out.println(base.toString());
                System.out.println("ID   : " + base.getId());
                System.out.println("Name : " + base.getName());
                System.out.println("Resources : ");
                base.getResources().forEach(k-> System.out.println(k.getCount() + "/" + k.getAvailableCapacity() + " (" + k.getProdPerHour() + "/h)"));
                System.out.println("Buildings : ");
                base.getBuildings().forEach(k-> {
                    System.out.println("\t" + k.getBuildingId() + " (lvl " + k.getCurrentLevel() + ")");
                    if (k.getEndsAt() != -1) {
                        System.out.println("\t\t Upgrading to lvl " + (k.getCurrentLevel() + 1) + ". Time left : " + (k.getEndsAt() - System.currentTimeMillis()) );
                    }
                    k.getModules().forEach(m-> System.out.println("\t\t" + m));
                });
                break;
            case "base.buildable":
                List<BuildingHolderDTO> buildings = BaseClient.getBuildables(GameContext.getInstance().getBase());
                buildings.forEach(k-> System.out.println(k.getTemplateId()));
                break;
        }

    }
}
