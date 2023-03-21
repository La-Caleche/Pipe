package fr.lacaleche.pipe.bukkit.modules.warps.commands;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.utils.Token;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.warps.arguments.WarpArgument;
import fr.lacaleche.pipe.bukkit.modules.warps.warp.WarpImpl;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.arguments.StringArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

@MinecraftCommand(label = "warp", aliases = {"w"}, arguments = {"name"}, description = "pipe.command.warps.description")

public class WarpCommand {
    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new WarpArgument("name"));
    }

    @CommandExecutor(executor = {CommandExecutor.Executor.PLAYER}, minPermLevel = 20)
    public boolean execute(Command<Player> command) {
        Client client = Pipe.get().getClient(command.sender().getUniqueId());
        WarpImpl warp = new ModelFilter<WarpImpl>().find(WarpImpl.class, w -> {
            return w.getName().equals(command.args().getString("name"));
        });
        if (warp == null) {
            command.sender().sendMessage(client.getLocale().t("pipe.command.warps.notfound").arg("name", command.args().getString("name")).ct());
            return true;
        }
        warp.teleport(command.sender());
        command.sender().sendMessage(client.getLocale().t("pipe.command.warps.teleport").arg("name", command.args().getString("name")).ct());
        return true;
    }

    @CommandChild(label = "delete", arguments = {"name"}, description = "pipe.command.warps.delete.description")
    public static class DeleteCommand {
        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new WarpArgument("name"));
        }

        @CommandExecutor(executor = {CommandExecutor.Executor.PLAYER}, minPermLevel = 20)
        public boolean execute(Command<Player> command) {
            Client client = Pipe.get().getClient(command.sender().getUniqueId());
            WarpImpl warp = new ModelFilter<WarpImpl>().find(WarpImpl.class, w -> {
                return w.getName().equals(command.args().getString("name"));
            });
            if (warp == null) {
                command.sender().sendMessage(client.getLocale().t("pipe.command.warps.notfound").arg("name", command.args().getString("name")).ct());
                return true;
            }

//            warp.delete();
            return true;
        }
    }

    @CommandChild(label = "list", description = "pipe.command.warps.list.description")
    public static class ListCommand {
        @CommandExecutor(executor = {CommandExecutor.Executor.PLAYER}, minPermLevel = 20)
        public boolean execute(Command<Player> command) {
            List<WarpImpl> warps = Core.get().getModelManager().get(WarpImpl.class).stream().toList();
            TextComponent.Builder message = Component.text();
            message.append(Component.text("Liste des warps: ", NamedTextColor.GOLD));
            message.append(Component.text(String.join(", ", warps.stream().map(WarpImpl::getName).toList()), NamedTextColor.AQUA));
            command.sender().sendMessage(message);
            return true;
        }
    }

    @CommandChild(label = "create", arguments = {"name"}, description = "pipe.command.warps.test.description")
    public static class TestCommand {
        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new StringArgument("name"));
        }

        @CommandExecutor(executor = {CommandExecutor.Executor.PLAYER}, minPermLevel = 20)
        public boolean execute(Command<Player> command) {
            Client client = Pipe.get().getClient(command.sender().getUniqueId());
            Location location = command.sender().getLocation();

            WarpImpl warp = new WarpImpl(command.args().getString("name"), location);
            command.sender().sendMessage(client.getLocale().t("pipe.command.warps.create").arg("name", command.args().getString("name")).ct());
            return true;
        }
    }
}
