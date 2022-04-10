package fr.lacaleche.pipe.bukkit.commands;

import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.Arrays;

public class SimpleBukkitPipeCommand extends BukkitCommand {

    public SimpleBukkitPipeCommand(MinecraftCommand command) {
        super(command.label());
        setAliases(Arrays.asList(command.aliases()));
        setDescription(command.description());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return true;
    }
}
