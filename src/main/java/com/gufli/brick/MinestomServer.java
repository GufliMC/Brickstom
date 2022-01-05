package com.gufli.brick;

import com.gufli.brick.commands.ReloadCommand;
import com.gufli.brick.commands.StopCommand;
import com.gufli.brick.generators.VoidGenerator;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.optifine.OptifineSupport;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import org.apache.commons.cli.*;

import java.nio.file.Path;

public class MinestomServer {

    public static void main(String[] args) {
        Options options = new Options();

        Option portOption = new Option("p", "port", true, "server port");
        portOption.setRequired(false);
        options.addOption(portOption);

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


        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setChunkGenerator(new VoidGenerator());
        instanceContainer.setChunkLoader(new AnvilLoader(Path.of("world")));

        MojangAuth.init();
        OptifineSupport.enable();

        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(.5, 63, .5));
        });

        MinecraftServer.getCommandManager().register(new StopCommand());
        MinecraftServer.getCommandManager().register(new ReloadCommand());

        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", port);
    }

}
