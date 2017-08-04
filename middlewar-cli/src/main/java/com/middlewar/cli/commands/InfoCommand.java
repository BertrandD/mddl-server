package com.middlewar.cli.commands;

import com.middlewar.cli.GameContext;
import com.middlewar.core.dto.AccountDTO;
import com.middlewar.core.dto.PlayerDTO;

/**
 * @author Bertrand
 */
public class InfoCommand extends Command{
    public InfoCommand() {
        super("info account", "Display info on the account");
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
        }

    }
}
