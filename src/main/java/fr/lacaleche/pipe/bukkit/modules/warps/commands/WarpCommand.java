package fr.lacaleche.pipe.bukkit.modules.warps.commands;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.utils.Token;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.warps.arguments.WarpArgument;
import fr.lacaleche.pipe.bukkit.modules.warps.events.WarpCreatedEvent;
import fr.lacaleche.pipe.bukkit.modules.warps.events.WarpDeletedEvent;
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
        manager.addArgument(new WarpArgument("warp"));
    }

    @CommandExecutor(executor = {CommandExecutor.Executor.PLAYER}, minPermLevel = 20)
    public boolean execute(Command<Player> command) {
        Client client = Pipe.getBukkit().getClient(command.sender().getUniqueId());
        String warpName = command.args().getString("warp");
        WarpImpl warp = new ModelFilter<WarpImpl>().model(WarpImpl.class)
                .cache(w -> w.getName().equalsIgnoreCase(warpName))
                .sql(sqlBuilder -> sqlBuilder.where(new Where("name", warpName))).getOne();
        if (warp == null) {
            command.sender().sendMessage(client.getLocale().t("pipe.command.warps.not_found").arg("name", warpName).ct());
            return true;
        }
        warp.teleport(command.sender());
        command.sender().sendMessage(client.getLocale().t("pipe.command.warps.teleport.success").arg("name", warpName).ct());
        return true;
    }

    @CommandChild(label = "delete", arguments = {"name"}, description = "pipe.command.warps.delete.description")
    public static class DeleteCommand {
        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new WarpArgument("warp"));
        }

        @CommandExecutor(executor = {CommandExecutor.Executor.PLAYER}, minPermLevel = 20)
        public boolean execute(Command<Player> command) {
            Client client = Pipe.getBukkit().getClient(command.sender().getUniqueId());
            String warpName = command.args().getString("warp");
            WarpImpl warp = new ModelFilter<WarpImpl>().model(WarpImpl.class)
                    .cache(w -> w.getName().equalsIgnoreCase(warpName))
                    .sql(sql -> sql.where(new Where("name", warpName))).getOne();
            if (warp == null) {
                command.sender().sendMessage(client.getLocale().t("pipe.command.warps.not_found").arg("name", warpName).ct());
                return true;
            }

            new WarpDeletedEvent(warp).call();
            warp.delete();
            command.sender().sendMessage(client.getLocale().t("pipe.command.warps.delete.success").arg("name", warpName).ct());
            return true;
        }
    }

    @CommandChild(label = "list", description = "pipe.command.warps.list.description")
    public static class ListCommand {
        @CommandExecutor(executor = {CommandExecutor.Executor.PLAYER}, minPermLevel = 20)
        public boolean execute(Command<Player> command) {
            Client client = Pipe.getBukkit().getClient(command.sender().getUniqueId());
            List<WarpImpl> warps = Core.get().getModelManager().get(WarpImpl.class).stream().toList();
            command.sender().sendMessage(client.getLocale().t("pipe.command.warps.list").arg("warps", String.join(", ", warps.stream().map(WarpImpl::getName).toList())).ct());
            return true;
        }
    }

    @CommandChild(label = "create", arguments = {"name"}, description = "pipe.command.warps.create.description")
    public static class CreateCommand {
        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new StringArgument("name"));
        }

        @CommandExecutor(executor = {CommandExecutor.Executor.PLAYER}, minPermLevel = 20)
        public boolean execute(Command<Player> command) {
            Client client = Pipe.getBukkit().getClient(command.sender().getUniqueId());
            Location location = command.sender().getLocation();
            String newWarpName = command.args().getString("name");

            WarpImpl existingWarp = new ModelFilter<WarpImpl>().model(WarpImpl.class).cache(w -> w.getName().equalsIgnoreCase(newWarpName)).getOne();
            if (existingWarp != null) {
                command.sender().sendMessage(client.getLocale().t("pipe.command.warps.already_exist").arg("name", newWarpName).ct());
                return true;
            }

            WarpImpl warp = new WarpImpl(newWarpName, location);
            new WarpCreatedEvent(warp).call();
            command.sender().sendMessage(client.getLocale().t("pipe.command.warps.create.success").arg("name", newWarpName).ct());
            return true;
        }
    }

    @CommandChild(label = "rename", arguments = {"warp", "newName"}, description = "pipe.command.warps.rename.description")
    public static class RenameCommand {
        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new WarpArgument("warp"));
            manager.addArgument(new StringArgument("newName"));
        }

        @CommandExecutor(executor = {CommandExecutor.Executor.PLAYER}, minPermLevel = 20)
        public boolean execute(Command<Player> command) {
            Client client = Pipe.getBukkit().getClient(command.sender().getUniqueId());
            String warpName = command.args().getString("warp");

            WarpImpl warp = new ModelFilter<WarpImpl>().model(WarpImpl.class)
                    .cache(w -> w.getName().equalsIgnoreCase(warpName))
                    .sql(sqlBuilder -> sqlBuilder.where(new Where("name", warpName))).getOne();
            if (warp == null) {
                command.sender().sendMessage(client.getLocale().t("pipe.command.warps.not_found").arg("name", warpName).ct());
                return true;
            }

            String newName = command.args().getString("newName");
            String oldName = warp.getName();

            WarpImpl existingWarp = new ModelFilter<WarpImpl>().model(WarpImpl.class).cache(w -> w.getName().equalsIgnoreCase(newName)).getOne();
            if (existingWarp != null) {
                command.sender().sendMessage(client.getLocale().t("pipe.command.warps.already_exist").arg("name", newName).ct());
                return true;
            }

            warp.setName(newName);
            command.sender().sendMessage(client.getLocale().t("pipe.command.warps.rename.success").arg("oldName", oldName).arg("name", newName).ct());
            return true;
        }
    }

    @CommandChild(label = "move", arguments = {"warp"}, description = "pipe.command.warps.move.description")
    public static class MoveCommand {
        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new WarpArgument("warp"));
        }

        @CommandExecutor(executor = {CommandExecutor.Executor.PLAYER}, minPermLevel = 20)
        public boolean execute(Command<Player> command) {
            Client client = Pipe.getBukkit().getClient(command.sender().getUniqueId());
            String warpName = command.args().getString("warp");
            Location newLocation = command.sender().getLocation();

            WarpImpl warp = new ModelFilter<WarpImpl>().model(WarpImpl.class)
                    .cache(w -> w.getName().equalsIgnoreCase(warpName))
                    .sql(sqlBuilder -> sqlBuilder.where(new Where("name", warpName))).getOne();
            if (warp == null) {
                command.sender().sendMessage(client.getLocale().t("pipe.command.warps.not_found").arg("name", warpName).ct());
                return true;
            }

            warp.setLocation(newLocation);

            command.sender().sendMessage(client.getLocale().t("pipe.command.warps.move.success").arg("name", warp.getName()).ct());
            return true;
        }
    }

}
