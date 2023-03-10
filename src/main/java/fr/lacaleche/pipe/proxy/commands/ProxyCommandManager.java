package fr.lacaleche.pipe.proxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.ServerInfo;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.utils.PipeCommandUtils;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.CoreCommandImpl;
import fr.lacaleche.pipe.common.commands.GlobalCommandManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Argument;
import fr.lacaleche.pipe.common.commands.enums.CommandResult;
import fr.lacaleche.pipe.common.commands.utils.CommandsUtils;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.proxy.commands.interfaces.IProxyCommandManager;
import fr.lacaleche.pipe.proxy.commands.interfaces.NetworkCommand;
import fr.lacaleche.pipe.proxy.utils.ProxyCommandUtils;

import java.util.*;

public class ProxyCommandManager extends GlobalCommandManager implements IProxyCommandManager {

    private Map<String, List<NetworkCommand>> networkCommands;

    public ProxyCommandManager() {
        super();

        this.networkCommands = new HashMap<>();
    }

    @Override
    public MinecraftCommand registerNewCommand(IModule module, Class<?> newCommand) {
        MinecraftCommand command = super.registerNewCommand(module, newCommand);
        ProxyCommandUtils.registerCommandAsBungee(command, new SimpleProxyPipeCommand(command));
        return command;
    }

    @Override
    public boolean unregisterCommand(IModule module, Class<?> unregistered) {
        if(!super.unregisterCommand(module, unregistered)) return false;
        ProxyCommandUtils.unregisterBungeeCommand(CommandsUtils.validateCommand(unregistered));
        return true;
    }

    @Override
    public void unregisterModuleCommands(IModule module) {
        List<Class<MinecraftCommand>> commands = this.getModuleCommands(module);
        if (commands == null || commands.size() == 0) return;
        List<Class<MinecraftCommand>> copy = new ArrayList<>(commands);

        for (Class<MinecraftCommand> command : copy) {
            this.unregisterCommand(module, command);
        }
    }

    @Override
    public CommandExecutor.Executor translateSender(Object source) {
        return ProxyCommandUtils.translateSender((CommandSource) source);
    }

    @Override
    public Client getClient(Object sender) {
        if (!(sender instanceof Player player)) return null;
        return Pipe.get().getClient(player.getUniqueId());
    }

    @Override
    public boolean validateSender(CommandExecutor executor, Object source) {
        return ProxyCommandUtils.validateSender(executor, (CommandSource) source);
    }

    @Override
    public void parseCommandResult(CoreCommandImpl command, Object objectSender, CommandResult result) {
        if (!(objectSender instanceof CommandSource source)) return;
        Locale locale = Pipe.get().getDefaultLocale();
        if (source instanceof Player player) locale = Pipe.get().getClient(player.getUniqueId()).getLocale();

        switch (result) {
            case MISSING_ARGUMENT -> {
                source.sendMessage(locale.t("pipe.helper.missing_arguments")
                        .arg("command", command.getLabel())
                        .arg("arguments", String.join(", ", this.getMissingArguments(command.getManager()).stream().map(Argument::getKey).toArray(String[]::new))).ct());
                return;
            }
        }
    }

    @Override
    public Map<String, List<NetworkCommand>> getNetworkCommands() {
        return networkCommands;
    }

    @Override
    public void registerNetworkCommand(String app, String host, String command) {
        List<NetworkCommand> commands = this.networkCommands.getOrDefault(host, new ArrayList<>());
        if (commands.stream().anyMatch(networkCommand -> networkCommand.label().equalsIgnoreCase(command))) return;
        commands.add(new NetworkCommandImpl(app, host, command));
        this.networkCommands.put(host, commands);
    }

    @Override
    public boolean isNetworkCommand(String host, String command) {
        if (!this.networkCommands.containsKey(host)) return false;
        return this.networkCommands.get(host).stream().anyMatch(networkCommand -> networkCommand.label().equalsIgnoreCase(command));
    }

    @Override
    public List<String> getCommandsFor(CommandSource commandSource) {
        if (commandSource instanceof ConsoleCommandSource) {
            return this.getCommands().keySet().stream().toList();
        } else if (commandSource instanceof Player player) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            if (client == null) return new ArrayList<>();
            return client.allowedCommands().stream().sorted().toList();
        }

        return Collections.emptyList();
    }

    @Override
    public List<String> getNetworkCommandsForPlayer(Player player) {
        Optional<ServerConnection> serverConnection = player.getCurrentServer();
        if (serverConnection.isEmpty()) return new ArrayList<>();
        return this.getNetworkCommandsForPlayer(player, serverConnection.get().getServerInfo());
    }

    @Override
    public List<String> getNetworkCommandsForPlayer(Player player, ServerInfo info) {
        if (info == null) return this.getNetworkCommandsForPlayer(player);
        return new ArrayList<>(this.networkCommands.get(info.getName()).stream().map(NetworkCommand::label).toList());
    }

    @Override
    public String getHostForPlayer(Player player) {
        Optional<ServerConnection> serverConnection = player.getCurrentServer();
        return serverConnection.map(connection -> connection.getServerInfo().getName()).orElse(null);
    }

    @Override
    public boolean commandExist(String label) {
        return ProxyCommandUtils.commandExist(label);
    }
}
