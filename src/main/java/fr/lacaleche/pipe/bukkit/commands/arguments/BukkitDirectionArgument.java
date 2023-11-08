package fr.lacaleche.pipe.bukkit.commands.arguments;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.argument.arguments.DefaultArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BukkitDirectionArgument extends DefaultArgument {

    public BukkitDirectionArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        completer.addAll(Arrays.stream(BlockFace.values()).map(BlockFace::name).collect(Collectors.toList()));
    }

}
