package fr.lacaleche.pipe.bukkit.commands.arguments;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.argument.arguments.DefaultArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BukkitPlayerArgument extends DefaultArgument {

    public BukkitPlayerArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        completer.addAll(Pipe.getBukkit().<JavaPlugin>getPlugin().getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
    }
}
