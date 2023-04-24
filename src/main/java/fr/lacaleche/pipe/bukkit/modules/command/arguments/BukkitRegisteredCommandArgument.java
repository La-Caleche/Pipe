package fr.lacaleche.pipe.bukkit.modules.command.arguments;

import com.velocitypowered.api.command.CommandSource;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.argument.arguments.DefaultArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;

import java.util.List;
import java.util.stream.Collectors;

public class BukkitRegisteredCommandArgument extends DefaultArgument {

    boolean withAliases = false;

    public BukkitRegisteredCommandArgument(String key) {
        super(key);
    }

    public BukkitRegisteredCommandArgument withAliases(boolean withAliases) {
        this.withAliases = withAliases;
        return this;
    }

    @Override
    public void completer(Completer completer) {
        CommandManager commandManager = Pipe.getBukkit().getCommandManager();
        List<String> commands = commandManager.getCommands().keySet().stream().toList();
        if (!withAliases) {
            commands = commands.stream().filter(s -> !s.startsWith("∅")).collect(Collectors.toList());
        }
        completer.addAll(commands);
    }
}
