package com.middlewar.cli.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.middlewar.cli.GameContext;
import com.middlewar.client.ClientConfig;

import java.io.File;
import java.io.IOException;

/**
 * @author Bertrand
 */
public class LoadCommand extends Command {
    public LoadCommand() {
        super("save", "Save the current context");
    }

    @Override
    public void exec() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            GameContext gameContext = mapper.readValue(new File("ctx.json"), GameContext.class);
            GameContext.setInstance(gameContext);
            if (gameContext.getAccount() != null) {
                ClientConfig.TOKEN = gameContext.getAccount().getToken();
            }

            System.out.println("Saved context loaded !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
