package com.middlewar.cli;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Bertrand
 */
public class ApplicationCLI {
    public static void main(String[] args) {
        System.out.println("Welcome to MiddleWar !");
        System.out.println("This tool is not the final game, it's more a dev tool we made for testing purpose");
        System.out.println("You can enjoy a greate part of the game, but some features are not yet available here");
        System.out.println("You can view the list of the available commands by typing \"help\"");

        if (args.length > 0) {
            try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {

                String sCurrentLine;

                while ((sCurrentLine = br.readLine()) != null) {
                    if (!sCurrentLine.isEmpty()) {
                        System.out.println("> "+sCurrentLine);
                        CommandHandler.handle(sCurrentLine);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        while (true) {

            System.out.print("> ");

            try {
                String input = CommandHandler.askForString();
                if ("q".equals(input)) {
                    System.out.println("Exit !");
                    System.exit(0);
                }

                CommandHandler.handle(input);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
