package org.minestombrick.brick.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop");

        setCondition((sender, commandString) ->
                sender instanceof ConsoleSender ||
                        sender.hasPermission("brick.stop") ||
                        (sender instanceof Player p && p.getPermissionLevel() == 4)
        );

        setDefaultExecutor((sender, context) -> stop());
    }

    private void stop() {
        MinecraftServer.stopCleanly();
    }

}
