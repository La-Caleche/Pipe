package fr.lacaleche.pipe.bukkit.modules.tab.commands;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.BukkitPipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.nametag.interfaces.PlayerNameTag;
import fr.lacaleche.pipe.bukkit.tabs.nametag.models.PersistentNametagImpl;
import fr.lacaleche.pipe.bukkit.utils.PipeCommandUtils;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.arguments.IntegerArgument;
import fr.lacaleche.pipe.common.commands.argument.arguments.StringArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

@MinecraftCommand(label = "tab", description = "pipe.command.tab.description", aliases = {"t"})
public class TabCommand {

    @CommandChild(label = "lines", description = "pipe.command.tab.lines.description", arguments = {"player"})
    public static class LinesCommand {

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").allowRandom().allowSelf().allowNearest());
        }

        @CommandExecutor(minPermLevel = 20)
        public boolean execute(Command<CommandSender> command) {
            BukkitPipe pipe = Pipe.getBukkit();
            Plugin plugin = pipe.getPlugin();
            Locale locale = command.locale();

            PipeCommandUtils.PlayerResult result = PipeCommandUtils.parseSelector(command.sender(), command.args(), "player");
            if (result.hasError()) {
                command.sender().sendMessage(result.getError().from("GameMode").ct());
                return true;
            }

            Player target = result.getPlayer();
            TabPlayer targetTab = pipe.getTabManager().getTabPlayer(target);
            PlayerNameTag targetNameTag = targetTab.getNameTag();

            command.sender().sendMessage(locale.t("pipe.command.tab.lines.list").arg("player", target.getName()).arg("lines", targetNameTag.getLines()).ct());
            return true;
        }

        @CommandChild(label = "save", description = "pipe.command.tab.lines.save.description", arguments = {"player", "order"})
        public static class SaveCommand {

            @ArgumentsManager
            public void manager(ArgumentManager manager) {
                manager.addArgument(new BukkitPlayerArgument("player").allowFull());
                manager.addArgument(new IntegerArgument("order"));
            }

            @CommandExecutor(minPermLevel = 70)
            public boolean execute(Command<CommandSender> command) {
                BukkitPipe pipe = Pipe.getBukkit();
                Plugin plugin = pipe.getPlugin();
                Locale locale = command.locale();

                PipeCommandUtils.PlayerResult result = PipeCommandUtils.parseSelector(command.sender(), command.args(), "player");
                if (result.hasError()) {
                    command.sender().sendMessage(result.getError().from("GameMode").ct());
                    return true;
                }

                Collection<Player> targets = result.getPlayers();

                targets.forEach(target -> {
                    Client targetClient = pipe.getClient(target.getUniqueId());
                    TabPlayer targetTab = pipe.getTabManager().getTabPlayer(target);
                    PlayerNameTag targetNameTag = targetTab.getNameTag();

                    int order = command.args().getInt("order");

                    if (!targetNameTag.hasLine(order)) {
                        command.sender().sendMessage(locale.t("pipe.command.tab.lines.save.not_found").arg("player", target.getName()).arg("order", order).ct());
                        return ;
                    }

                    PersistentNametagImpl newPersistentNametag = new ModelFilter<PersistentNametagImpl>().model(PersistentNametagImpl.class)
                            .cache(persistentNametag -> persistentNametag.getClient().getId() == targetClient.getId() && persistentNametag.getIndexOrder() == order)
                            .sql(sqlBuilder -> sqlBuilder.where(new Where("client_id", targetClient.getId())).where(new Where("index_order", order)))
                            .def(() -> new PersistentNametagImpl((ClientImpl) targetClient, order, targetNameTag.getLine(order).getLeft()))
                            .getOne();

                    if (newPersistentNametag == null) {
                        command.sender().sendMessage(locale.t("pipe.command.tab.lines.save.error").arg("player", target.getName()).arg("order", order).ct());
                    }
                });

                command.sender().sendMessage(locale.t("pipe.command.tab.lines.save.success").ct());
                return true;
            }

        }

        @CommandChild(label = "set", description = "pipe.command.tab.lines.edit.description", arguments = {"player", "order", "text"})
        public static class SetCommand {

            @ArgumentsManager
            public void manager(ArgumentManager manager) {
                manager.addArgument(new BukkitPlayerArgument("player").allowFull());
                manager.addArgument(new IntegerArgument("order"));
                manager.addArgument(new StringArgument("text").setMultiple(true).optional());
            }

            @CommandExecutor(minPermLevel = 70)
            public boolean execute(Command<CommandSender> command) {
                BukkitPipe pipe = Pipe.getBukkit();
                Plugin plugin = pipe.getPlugin();
                Locale locale = command.locale();

                PipeCommandUtils.PlayerResult result = PipeCommandUtils.parseSelector(command.sender(), command.args(), "player");
                if (result.hasError()) {
                    command.sender().sendMessage(result.getError().from("GameMode").ct());
                    return true;
                }

                Collection<Player> targets = result.getPlayers();
                String text = command.args().getString("text");

                targets.forEach(target -> {
                    TabPlayer targetTab = pipe.getTabManager().getTabPlayer(target);
                    PlayerNameTag targetNameTag = targetTab.getNameTag();
                    int order = command.args().getInt("order");

                    if (command.args().blank("text")) {
                        if (targetNameTag.hasLine(order)) {
                            targetNameTag.removeLine(order);
                            pipe.getTabManager().refreshPlayer(targetTab);
                        }
                        return ;
                    }

                    targetNameTag.addLine(text, order);
                    pipe.getTabManager().refreshPlayer(targetTab);
                });

                command.sender().sendMessage(locale.ct("pipe.command.tab.lines.edit.removed", "pipe.command.tab.lines.edit.set", command.args().blank("text")).arg("text", text).ct());
                return true;
            }

        }

    }

}
