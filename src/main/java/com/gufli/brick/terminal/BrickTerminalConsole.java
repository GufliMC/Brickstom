package com.gufli.brick.terminal;

import net.minecrell.terminalconsole.SimpleTerminalConsole;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

public class BrickTerminalConsole extends SimpleTerminalConsole {

    private final CommandManager commandManager = MinecraftServer.getCommandManager();

    @Override
    protected LineReader buildReader(LineReaderBuilder builder) {
        return super.buildReader(builder
                .appName("Brick")
                .completer(new BrickConsoleCommandCompleter()));
    }

    @Override
    protected boolean isRunning() {
        return MinecraftServer.isStarted() && !MinecraftServer.isStopping();
    }

    @Override
    protected void runCommand(String command) {
        commandManager.execute(commandManager.getConsoleSender(), command);
    }

    @Override
    protected void shutdown() {
        try {
            MinecraftServer.stopCleanly();
        } catch (Throwable t) {
            System.exit(1);
        }
    }

}
