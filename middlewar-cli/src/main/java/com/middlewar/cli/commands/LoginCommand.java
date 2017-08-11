package com.middlewar.cli.commands;

import com.middlewar.cli.CommandHandler;
import com.middlewar.cli.GameContext;
import com.middlewar.client.AccountClient;
import com.middlewar.dto.AccountDTO;

/**
 * @author Bertrand
 */
public class LoginCommand extends Command{
    public LoginCommand() {
        super("login <username>", "Log in on the middlewar server");
    }

    @Override
    public void exec() {
        if (getInput().length <= 1) {
            printUsage();
            return;
        }

        System.out.println("Password : ");
        String password = CommandHandler.askForString();

        AccountDTO account = AccountClient.login(getInput()[1], password);
        if (account == null) return;

        GameContext.getInstance().setAccount(account);

        System.out.println("Logged in as " + account.getUsername() + "! ");

        if (account.getPlayers().size() == 0) {
            System.out.println("You don't have any player. You should create one with  with " + CreateCommand.USAGE);
            return;
        }

        GameContext.getInstance().setPlayer(account.getPlayers().get(0));
    }
}
