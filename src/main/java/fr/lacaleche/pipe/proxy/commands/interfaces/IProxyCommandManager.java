package fr.lacaleche.pipe.proxy.commands.interfaces;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.ServerInfo;

import java.util.List;
import java.util.Map;

public interface IProxyCommandManager {

    void registerNetworkCommand(String app, String host, String command);

    Map<String, List<NetworkCommand>> getNetworkCommands();

    boolean isNetworkCommand(String host, String command);

    List<String> getCommandsFor(CommandSource commandSource);

    List<String> getNetworkCommandsForPlayer(Player commandSource);

    List<String> getNetworkCommandsForPlayer(Player commandSource, ServerInfo info);

    String getHostForPlayer(Player player);

}
