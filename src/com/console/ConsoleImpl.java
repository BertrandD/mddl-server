package com.console;

import com.handler.CommandHandler;
import com.handler.ICommandHandler;

import java.util.Scanner;

/**
 * @author LEBOC Philippe
 */
public class ConsoleImpl implements Runnable {

    private Scanner scanner;

    public ConsoleImpl(Scanner sc) {
        scanner = sc;
    }

    @Override
    public void run() {
        System.out.println("Console started.\r\nYou can write command here to handle some functions.");
        System.out.print("> ");
        while(true) {
            while (scanner.hasNext()) {
                System.out.print("> ");
                String inStr = scanner.nextLine();
                String command = inStr;
                String params = null;

                if(inStr.contains(" ")) {
                    command = inStr.substring(0, inStr.indexOf(" "));
                    params = inStr.substring(inStr.indexOf(" ")+1, inStr.length());
                }

                final ICommandHandler handler = CommandHandler.getInstance().getHandler(command);
                if(handler != null){
                    handler.useCommand(command, params);
                } else System.out.println("Unknown command");
            }
        }
    }
}
