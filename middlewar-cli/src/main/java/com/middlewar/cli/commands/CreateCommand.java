package com.middlewar.cli.commands;

import com.middlewar.cli.CommandHandler;
import com.middlewar.cli.GameContext;
import com.middlewar.client.BaseClient;
import com.middlewar.client.PlayerClient;
import com.middlewar.dto.BaseDTO;
import com.middlewar.dto.PlayerDTO;

/**
 * @author Bertrand
 */
public class CreateCommand extends Command{

    public static String USAGE = "create <player|base>";

    public CreateCommand() {
        super(USAGE, "Create a player");
    }

    @Override
    public void exec() {
        if (getInput().length <= 1) {
            printUsage();
            return;
        }
        switch (getInput()[1]) {
            case "player":
                if (GameContext.getInstance().getPlayer() != null) {
                    System.out.println("You already have a player.");
                    return;
                }

                System.out.println("Name for the player : ");
                String name = CommandHandler.askForString();

                PlayerDTO player = PlayerClient.createPlayer(name);
                if (player == null) return;

                GameContext.getInstance().setPlayer(player);

                System.out.println("Player " + player.getName() + " created ! You should now create a base !");
                break;
            case "base":
                if (GameContext.getInstance().getBase() != null) {
                    System.out.println("You already have a base.");
                    return;
                }
                System.out.println("Name for the base : ");
                name = CommandHandler.askForString();

                BaseDTO base = BaseClient.createBase(name);
                if (base == null) return;

                GameContext.getInstance().setBase(base);

                System.out.println("Base " + base.getName() + " created ! ");
                break;
            default:
                System.out.println("Unknown option : " + getInput()[1]);
                printUsage();
                break;
        }

    }
}
