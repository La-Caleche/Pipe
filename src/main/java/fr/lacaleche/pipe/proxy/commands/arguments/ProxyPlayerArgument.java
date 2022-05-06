package fr.lacaleche.pipe.proxy.commands.arguments;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.argument.arguments.DefaultArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.entity.HumanEntity;

import java.util.stream.Collectors;

public class ProxyPlayerArgument extends DefaultArgument {

    public ProxyPlayerArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        completer.addAll(Pipe.get().<Plugin>getPlugin().getProxy().getPlayers().stream().map(CommandSender::getName).collect(Collectors.toList()));
    }
}
