package fr.lacaleche.pipe.proxy.commands.arguments;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.argument.arguments.DefaultArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.proxy.ProxyPlugin;
import org.bukkit.entity.HumanEntity;

import java.util.stream.Collectors;

public class ProxyPlayerArgument extends DefaultArgument {

    public ProxyPlayerArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        completer.addAll(Pipe.get().<ProxyPlugin>getPlugin().getServer().getAllPlayers().stream().map(Player::getUsername).collect(Collectors.toList()));
    }
}
