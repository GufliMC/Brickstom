package com.guflimc.brickstom;

import com.guflimc.brickstom.commands.StopCommand;
import com.guflimc.brickstom.terminal.BrickTerminalConsole;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerCommandEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.optifine.OptifineSupport;
import net.minestom.server.extras.velocity.VelocityProxy;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class Brickstom {

    private final static Logger logger = LoggerFactory.getLogger(Brickstom.class);

    public static void main(String[] args) {
        long initTime = System.nanoTime();

        logger.info("Brickstom is starting...");

        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        // command line options
        Options options = new Options();

        Option portOption = new Option("p", "port", true, "server port");
        options.addOption(portOption);

        Option offlineModeOption = new Option("o", "offline-mode", false, "disable mojang auth");
        options.addOption(offlineModeOption);

        Option proxySecretOption = new Option("ps", "proxy-secret", true, "enable proxy protection");
        options.addOption(proxySecretOption);

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

        // set server port
        int port = 25565;
        if ( cmd.hasOption(portOption) ) {
            String portStr = cmd.getOptionValue(portOption);
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
        if ( !cmd.hasOption(offlineModeOption) ) {
            MojangAuth.init();
        }

        // proxy protection
        if ( cmd.hasOption(proxySecretOption) ) {
            VelocityProxy.enable(cmd.getOptionValue(proxySecretOption));
        }

        // optifine enhancement
        OptifineSupport.enable();

        // default unknown command handler
        MinecraftServer.getCommandManager().setUnknownCommandCallback((sender, command) -> {
            sender.sendMessage("Unknown command.");
        });

        // log players executing commands
        MinecraftServer.getGlobalEventHandler().addListener(PlayerCommandEvent.class, e -> {
            if ( e.isCancelled() ) return;
            logger.info("{} issued command: {}", e.getPlayer().getUsername(), e.getCommand());
        }).setPriority(Integer.MAX_VALUE);

        // default commands
        MinecraftServer.getCommandManager().register(new StopCommand());

        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", port);

        long diff = System.nanoTime() - initTime;
        logger.info(String.format("\u00A7bServer started in %.2fs.", diff / 1000000000f));

        // enable better terminal
        new BrickTerminalConsole().start();
    }

}
