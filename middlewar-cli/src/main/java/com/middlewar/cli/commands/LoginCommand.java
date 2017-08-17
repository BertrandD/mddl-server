package com.middlewar.cli.commands;

import com.middlewar.cli.GameContext;
import com.middlewar.client.AccountClient;
import com.middlewar.client.BaseClient;
import com.middlewar.client.PlayerClient;
import com.middlewar.dto.AccountDTO;
import com.middlewar.dto.PlayerDTO;

/**
 * @author Bertrand
 */
public class LoginCommand extends Command {
    public LoginCommand() {
        super("login <username> <password>", "Log in on the middlewar server");
    }

    @Override
    public void exec() {
        String username = getParam("Username", 1);
        String password = getParam("Password", 2);

        AccountDTO account = AccountClient.login(username, password);
        if (account == null) return;

        GameContext.getInstance().setAccount(account);

        if (account.getCurrentPlayer() > 0) {
            PlayerDTO playerDTO = PlayerClient.getPlayer(account.getCurrentPlayer());
            GameContext.getInstance().setPlayer(playerDTO);

            if (playerDTO.getCurrentBase() > 0) {
                GameContext.getInstance().setBase(BaseClient.getBase(playerDTO.getCurrentBase()));
            }
        }

        System.out.println("Logged in as " + account.getUsername() + "! ");
    }
}
