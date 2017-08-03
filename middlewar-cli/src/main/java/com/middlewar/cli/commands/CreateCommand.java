package com.middlewar.cli.commands;

import com.middlewar.cli.APICall;
import com.middlewar.cli.CommandHandler;
import com.middlewar.cli.GameContext;
import com.middlewar.core.model.Player;

/**
 * @author Bertrand
 */
public class CreateCommand extends Command{
    public CreateCommand() {
        super("create player", "Create a player");
    }

    @Override
    public void exec() {
        if (getInput().length <= 1) {
            printUsage();
            return;
        }

        if (getInput()[1].equals("player")) {

            System.out.println("Name for the player : ");
            String name = CommandHandler.askForString();

            Player player = APICall.createPlayer(name);
            if (player == null) return;

            GameContext.getInstance().setPlayer(player);

            System.out.println("Player " + player.getName() + " created ! ");
        }

    }
}
