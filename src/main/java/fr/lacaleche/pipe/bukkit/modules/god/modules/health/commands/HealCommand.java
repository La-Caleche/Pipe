package fr.lacaleche.pipe.bukkit.modules.god.modules.health.commands;

import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.bukkit.modules.command.utils.BukkitEntitySelector;
import fr.lacaleche.pipe.bukkit.modules.god.modules.health.HealthModule;
import fr.lacaleche.pipe.common.commands.annotations.*;
import fr.lacaleche.pipe.common.commands.argument.arguments.CustomArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import fr.lacaleche.pipe.common.commands.utils.EntitySelectorResult;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@MinecraftCommand(label = "heal", description = "pipe.command.heal.description", arguments = {"player"})
public class HealCommand {

    @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.heal")
    public boolean execute(Command<CommandSender> command) {
        EntitySelectorResult<Player> result = BukkitEntitySelector.parsePlayers(command, "player");
        if (result.hasError()) {
            command.sender().sendMessage(result.getError().from("Heal").ct());
            return true;
        }

        Collection<Player> targets = result.getEntities();

        targets.forEach(target -> {
            target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            target.setFoodLevel(20);
        });

        command.sender().sendMessage(command.locale().ct("pipe.command.heal.success.one", "pipe.command.heal.success.all", targets.size() == 1).ph("player", result.first()).from("Heal").ct());

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitPlayerArgument("player").allowFull().optional());
    }

    @CommandChild(label = "get", arguments = {"player"}, description = "pipe.command.heal.get.description")
    public static class Get {

        @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.heal.get")
        public boolean execute(Command<CommandSender> command) {
            EntitySelectorResult<Player> result = BukkitEntitySelector.parsePlayers(command, "player");
            if (result.hasError()) {
                command.sender().sendMessage(result.getError().from("Heal").ct());
                return true;
            }

            Player target = result.first();

            command.sender().sendMessage(command.locale().t("pipe.command.heal.get_health").ph("player", target).arg("health", target.getHealth()).arg("max_health", target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()).from("Heal").ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").allowRandom().allowSelf().allowNearest());
        }

    }

    @CommandChild(label = "reasons", description = "pipe.command.heal.reasons.description")
    public static class Reasons {

        @CommandChild(label = "add", description = "pipe.command.heal.reasons.add.description")
        public static class Add {

            @ArgumentsManager
            public void manager(ArgumentManager manager) {
                manager.addArgument(new CustomArgument("reason"));
            }

            @TabCompleter
            public void completer(Completer completer) {
                if (completer.index() == 1) {
                    List<EntityRegainHealthEvent.RegainReason> causes = Arrays.stream(EntityRegainHealthEvent.RegainReason.values()).toList();
                    List<EntityRegainHealthEvent.RegainReason> blackList = Core.getModule(HealthModule.class).getBlackListRegainReason();
                    completer.addAll(causes.stream().filter(cause -> !blackList.contains(cause)).map(EntityRegainHealthEvent.RegainReason::name).collect(Collectors.toList()));
                }
            }

            @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.heal.reasons.add")
            public boolean execute(Command<CommandSender> command) {
                HealthModule module = Core.getModule(HealthModule.class);
                String reasonName = command.args().getString("reason");
                EntityRegainHealthEvent.RegainReason reason = Arrays.stream(EntityRegainHealthEvent.RegainReason.values()).filter(r -> r.name().equals(reasonName)).findFirst().orElse(null);

                if (reason == null) {
                    command.sender().sendMessage(command.locale().t("pipe.command.heal.reasons.invalid_reason").arg("reason", reasonName).from("Heal").ct());
                    return true;
                }

                module.blackListRegainReason(reason);
                command.sender().sendMessage(command.locale().t("pipe.command.heal.reasons.added").arg("reason", reasonName).from("Heal").ct());
                return true;
            }

        }

        @CommandChild(label = "remove", description = "pipe.command.heal.reasons.remove.description")
        public static class Remove {

            @ArgumentsManager
            public void manager(ArgumentManager manager) {
                manager.addArgument(new CustomArgument("reason"));
            }

            @TabCompleter
            public void completer(Completer completer) {
                if (completer.index() == 1) {
                    if (completer.index() == 1) {
                        List<EntityRegainHealthEvent.RegainReason> causes = Arrays.stream(EntityRegainHealthEvent.RegainReason.values()).toList();
                        List<EntityRegainHealthEvent.RegainReason> blackList = Core.getModule(HealthModule.class).getBlackListRegainReason();
                        completer.addAll(causes.stream().filter(blackList::contains).map(EntityRegainHealthEvent.RegainReason::name).collect(Collectors.toList()));
                    }
                }
            }

            @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.heal.reasons.remove")
            public boolean execute(Command<CommandSender> command) {
                HealthModule module = Core.getModule(HealthModule.class);
                String reasonName = command.args().getString("reason");
                EntityRegainHealthEvent.RegainReason reason = Arrays.stream(EntityRegainHealthEvent.RegainReason.values()).filter(r -> r.name().equals(reasonName)).findFirst().orElse(null);

                if (reason == null) {
                    command.sender().sendMessage(command.locale().t("pipe.command.heal.reasons.invalid_reason").arg("reason", reasonName).from("Heal").ct());
                    return true;
                }

                module.unblackListRegainReason(reason);
                command.sender().sendMessage(command.locale().t("pipe.command.heal.reasons.removed").arg("reason", reasonName).from("Heal").ct());
                return true;
            }

        }

    }


}
