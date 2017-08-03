package com.middlewar.cli.commands;

import com.middlewar.cli.GameContext;
import com.middlewar.core.model.Account;

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


        Account account = GameContext.getInstance().getAccount();
        if (account == null) {
            System.out.println("You are not logged in");
            return;
        }

        System.out.println("Id       : " + account.getId());
        System.out.println("Username : " + account.getUsername());
    }
}
