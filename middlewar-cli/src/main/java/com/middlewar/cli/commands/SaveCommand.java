package com.middlewar.cli.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.middlewar.cli.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * @author Bertrand
 */
@Slf4j
public class SaveCommand extends Command {
    public SaveCommand() {
        super("save", "Save the current context");
    }

    @Override
    public void exec() {

        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(new File("ctx.json"), GameContext.getInstance());
            String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(GameContext.getInstance());
            log.debug(jsonInString);
            System.out.println("Context saved !");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
