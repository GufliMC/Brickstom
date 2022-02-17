package org.minestombrick.brick.terminal;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandSyntax;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.parser.ArgumentQueryResult;
import net.minestom.server.command.builder.parser.CommandParser;
import net.minestom.server.command.builder.parser.CommandQueryResult;
import net.minestom.server.utils.StringUtils;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BrickConsoleCommandCompleter implements Completer {

    private final CommandManager commandManager = MinecraftServer.getCommandManager();
    private final ConsoleSender sender = commandManager.getConsoleSender();

    @Override
    public void complete(LineReader reader, ParsedLine parsedLine, List<Candidate> candidates) {
        final String commandString = parsedLine.line();
        final String[] args = commandString.split(StringUtils.SPACE, -1);

        if (args.length == 1) {
            final String cmdName = args[args.length - 1];
            candidates.addAll(autoCompleteCommand(commandManager.getDispatcher().getCommands(), cmdName));
            return;
        }

        final CommandQueryResult commandQueryResult = CommandParser.findCommand(MinecraftServer.getCommandManager().getDispatcher(), commandString);
        if (commandQueryResult == null) {
            return;
        }

        final ArgumentQueryResult queryResult = CommandParser.findEligibleArgument(commandQueryResult.command(),
                commandQueryResult.args(), commandString, commandString.endsWith(StringUtils.SPACE), false,
                CommandSyntax::hasSuggestion, Argument::hasSuggestion);
        if (queryResult == null) {
            final String cmdName = args[args.length - 1];
            candidates.addAll(autoCompleteCommand(commandQueryResult.command().getSubcommands(), cmdName));
            return;
        }

        final Argument<?> argument = queryResult.argument();
        if ( !argument.hasSuggestion() ) {
            return;
        }

        // TODO
    }

    private List<Candidate> autoCompleteCommand(Collection<Command> commands, String cmdName) {
        return commands.stream()
                .flatMap(cmd -> {
                    if (cmd.getAliases().length > 0) {
                        return Arrays.stream(cmd.getAliases()); // shorter cmd versions
                    }
                    return Stream.of(cmd.getName());
                })
                .filter(cmd -> cmd.contains(cmdName))
                .map(Candidate::new)
                .collect(Collectors.toList());
    }
}
