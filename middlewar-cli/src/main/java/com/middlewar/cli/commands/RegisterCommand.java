package com.middlewar.cli.commands;

import com.middlewar.cli.APICall;
import com.middlewar.cli.CommandHandler;
import com.middlewar.cli.GameContext;
import com.middlewar.core.model.Account;

/**
 * @author Bertrand
 */
public class RegisterCommand extends Command{
    public RegisterCommand() {
        super("register <username>", "Register on the middlewar server");
    }

    @Override
    public void exec() {
        if (getInput().length <= 1) {
            printUsage();
            return;
        }

        System.out.println("Password : ");
        String password = CommandHandler.askForString();

        Account account = APICall.register(getInput()[1], password);
        if (account == null) return;

        GameContext.getInstance().setAccount(account);

        System.out.println("You are now registered ! ");
    }
}
