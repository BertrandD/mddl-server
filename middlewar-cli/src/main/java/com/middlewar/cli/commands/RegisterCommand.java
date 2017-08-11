package com.middlewar.cli.commands;

import com.middlewar.cli.CommandHandler;
import com.middlewar.cli.GameContext;
import com.middlewar.client.AccountClient;
import com.middlewar.dto.AccountDTO;

/**
 * @author Bertrand
 */
public class RegisterCommand extends Command{
    public RegisterCommand() {
        super("register <username> <password>", "Register on the middlewar server");
    }

    @Override
    public void exec() {
        String username = getParam("Username", 1);
        String password = getParam("Password", 2);

        AccountDTO account = AccountClient.register(username, password);
        if (account == null) return;

        GameContext.getInstance().setAccount(account);

        System.out.println("You are now registered ! You should create a player with " + CreateCommand.USAGE);
    }
}
