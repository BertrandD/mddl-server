package com.middlewar.cli.commands;

import com.middlewar.cli.GameContext;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;

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
                Account account = GameContext.getInstance().getAccount();
                if (account == null) {
                    System.out.println("You are not logged in");
                    return;
                }

                System.out.println("ID       : " + account.getId());
                System.out.println("Username : " + account.getUsername());
                break;
            case "player":
                Player player = GameContext.getInstance().getPlayer();
                if (player == null) {
                    System.out.println("You don't have any player. Let's create it !");
                    return;
                }
                System.out.println("ID :");
                System.out.println("Name :");
                break;
        }

    }
}
