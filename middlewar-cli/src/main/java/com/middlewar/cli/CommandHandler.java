package com.middlewar.cli;

import com.middlewar.cli.commands.BuildCommand;
import com.middlewar.cli.commands.Command;
import com.middlewar.cli.commands.CreateCommand;
import com.middlewar.cli.commands.HelpCommand;
import com.middlewar.cli.commands.InfoCommand;
import com.middlewar.cli.commands.LoadCommand;
import com.middlewar.cli.commands.LoginCommand;
import com.middlewar.cli.commands.RegisterCommand;
import com.middlewar.cli.commands.SaveCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Bertrand
 */
public class CommandHandler {

    public static Map<String, Command> COMMANDS = new HashMap<>();
    public static Map<String, String> ALIASES = new HashMap<>();
    private static Scanner scanIn = new Scanner(System.in);

    public static String askForString() {
        try {
            return scanIn.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return "";
        }
    }

    public static void init() {
        COMMANDS.put("info", new InfoCommand());
        COMMANDS.put("register", new RegisterCommand());
        COMMANDS.put("login", new LoginCommand());
        COMMANDS.put("create", new CreateCommand());
        COMMANDS.put("help", new HelpCommand());
        COMMANDS.put("save", new SaveCommand());
        COMMANDS.put("load", new LoadCommand());
        COMMANDS.put("build", new BuildCommand());

        ALIASES.put("b", "build");
        ALIASES.put("i", "info");
        ALIASES.put("c", "create");
        ALIASES.put("l", "login");
        ALIASES.put("r", "register");
        ALIASES.put("h", "help");
        ALIASES.put("?", "help");
    }

    public static void handle(String input) {
        if (COMMANDS.isEmpty()) init();

        String[] parts = input.split(" ");
        String command = parts[0];

        if (!COMMANDS.containsKey(command)) {
            if (!ALIASES.containsKey(command)) {
                System.out.print(command.length() > 0 ? "Unknown command \"" + command + "\" \n" : "");
                return;
            }
            command = ALIASES.get(command);
        }

        Command com = COMMANDS.get(command);
        com.setInput(parts);
        com.exec();
    }
}
