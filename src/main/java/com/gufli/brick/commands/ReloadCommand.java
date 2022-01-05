package com.gufli.brick.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.extensions.Extension;
import net.minestom.server.extensions.ExtensionManager;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reload");

        setCondition((sender, commandString) ->
                sender instanceof ConsoleSender ||
                sender.hasPermission("brick.reload"));

        setDefaultExecutor((sender, context) -> reload());
    }

    private void reload() {
        MinecraftServer.LOGGER.info("Reloading extensions...");

        ExtensionManager extensionManager = MinecraftServer.getExtensionManager();
        extensionManager.getExtensions().forEach(ext -> extensionManager.reload(ext.getOrigin().getName()));
    }

}
