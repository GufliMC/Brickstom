package com.gufli.brick.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.extensions.Extension;
import net.minestom.server.extensions.ExtensionManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        extensionManager.shutdown();

        // I know it's dirty
        // TODO find better way
        try {
            Method loadExtensions = extensionManager.getClass().getDeclaredMethod("loadExtensions");
            loadExtensions.setAccessible(true);
            loadExtensions.invoke(extensionManager);

            // somehow the initialize method is not called
            for (Extension ext : extensionManager.getExtensions()) {
                try {
                    ext.initialize();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
