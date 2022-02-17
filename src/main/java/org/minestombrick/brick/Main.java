package org.minestombrick.brick;

import org.minestombrick.brick.commands.StopCommand;
import org.minestombrick.brick.terminal.BrickTerminalConsole;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.optifine.OptifineSupport;
import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {
        Options options = new Options();

        Option portOption = new Option("p", "port", true, "server port");
        portOption.setRequired(false);
        options.addOption(portOption);

        Option offlineModeOption = new Option("o", "offline-mode", false, "disable mojang auth");
        offlineModeOption.setRequired(false);
        options.addOption(offlineModeOption);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("[..options]", options);
            System.exit(1);
            return;
        }

        int port = 25565;
        if ( cmd.hasOption("port") ) {
            String portStr = cmd.getOptionValue("port");
            if ( !portStr.matches("[0-9]{4,5}") ) {
                throw new IllegalArgumentException("Port is invalid.");
            }
            port = Integer.parseInt(portStr);
        }

        // disable terminal
        System.setProperty("minestom.terminal.disabled", "false");

        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();

        // offline-mode
        if ( !cmd.hasOption("offline-mode") ) {
            MojangAuth.init();
        }

        OptifineSupport.enable();

        MinecraftServer.getCommandManager().setUnknownCommandCallback((sender, command) -> {
            sender.sendMessage("Unknown command.");
        });

        MinecraftServer.getCommandManager().register(new StopCommand());

        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", port);

        // enable better terminal
        new BrickTerminalConsole().start();
    }

}
