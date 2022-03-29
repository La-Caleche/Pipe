package fr.lacaleche.pipe.commands;

import fr.lacaleche.base.commands.annotations.MinecraftCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.Arrays;

public class SimplePipeCommand extends BukkitCommand {

    public SimplePipeCommand(MinecraftCommand command) {
        super(command.label());
        setAliases(Arrays.asList(command.aliases()));
        setDescription(command.description());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return true;
    }
}
