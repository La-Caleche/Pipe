package fr.lacaleche.pipe.bukkit.commands.arguments;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.argument.arguments.DefaultArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public class BukkitWorldArgument extends DefaultArgument {

    public BukkitWorldArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        completer.addAll(Pipe.getBukkit().<JavaPlugin>getPlugin().getServer().getWorlds().stream().map(WorldInfo::getName).collect(Collectors.toList()));
    }

}
