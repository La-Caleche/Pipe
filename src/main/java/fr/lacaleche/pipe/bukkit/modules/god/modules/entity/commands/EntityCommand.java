package fr.lacaleche.pipe.bukkit.modules.god.modules.entity.commands;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.pipe.bukkit.modules.god.modules.entity.EntityModule;
import fr.lacaleche.pipe.common.commands.annotations.*;
import fr.lacaleche.pipe.common.commands.argument.arguments.CustomArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@MinecraftCommand(label = "damage", description = "pipe.command.entity.description")
public class EntityCommand {

    @CommandChild(label = "damage-causes", description = "pipe.command.entity.damage_causes.description")
    public static class DamageCauses {

        @CommandExecutor
        public boolean execute(Command<CommandSender> command) {
            return true;
        }

        @CommandChild(label = "add", description = "pipe.command.entity.damage_causes.add.description")
        public static class Add {

            @ArgumentsManager
            public void manager(ArgumentManager manager) {
                manager.addArgument(new CustomArgument("cause"));
            }

            @TabCompleter
            public void completer(Completer completer) {
                if (completer.index() == 1) {
                    List<EntityDamageEvent.DamageCause> causes = Arrays.stream(EntityDamageEvent.DamageCause.values()).toList();
                    List<EntityDamageEvent.DamageCause> blackList = CalecheCore.get().getCentralModuleManager().getModule(EntityModule.class).getBlackListDamageCauses();
                    completer.addAll(causes.stream().filter(cause -> !blackList.contains(cause)).map(EntityDamageEvent.DamageCause::name).collect(Collectors.toList()));
                }
            }

            @CommandExecutor
            public boolean execute(Command<CommandSender> command) {
                EntityModule module = CalecheCore.get().getCentralModuleManager().getModule(EntityModule.class);
                String causeName = command.args().getString("cause");
                EntityDamageEvent.DamageCause cause = Arrays.stream(EntityDamageEvent.DamageCause.values()).filter(damageCause -> damageCause.name().equals(causeName)).findFirst().orElse(null);

                if (cause == null) {
                    command.sender().sendMessage(command.locale().t("pipe.command.entity.damage_causes.invalid_cause").arg("cause", causeName).from("Entity").ct());
                    return true;
                }

                module.blackListDamageCause(cause);
                command.sender().sendMessage(command.locale().t("pipe.command.entity.damage_causes.added").arg("cause", causeName).from("Entity").ct());
                return true;
            }

        }

        @CommandChild(label = "remove", description = "pipe.command.entity.damage_causes.remove.description")
        public static class Remove {

            @ArgumentsManager
            public void manager(ArgumentManager manager) {
                manager.addArgument(new CustomArgument("cause"));
            }

            @TabCompleter
            public void completer(Completer completer) {
                if (completer.index() == 1) {
                    if (completer.index() == 1) {
                        List<EntityDamageEvent.DamageCause> causes = Arrays.stream(EntityDamageEvent.DamageCause.values()).toList();
                        List<EntityDamageEvent.DamageCause> blackList = CalecheCore.get().getCentralModuleManager().getModule(EntityModule.class).getBlackListDamageCauses();
                        completer.addAll(causes.stream().filter(blackList::contains).map(EntityDamageEvent.DamageCause::name).collect(Collectors.toList()));
                    }
                }
            }

            @CommandExecutor
            public boolean execute(Command<CommandSender> command) {
                EntityModule module = CalecheCore.get().getCentralModuleManager().getModule(EntityModule.class);
                String causeName = command.args().getString("cause");
                EntityDamageEvent.DamageCause cause = Arrays.stream(EntityDamageEvent.DamageCause.values()).filter(damageCause -> damageCause.name().equals(causeName)).findFirst().orElse(null);

                if (cause == null) {
                    command.sender().sendMessage(command.locale().t("pipe.command.entity.damage_causes.invalid_cause").arg("cause", causeName).from("Entity").ct());
                    return true;
                }

                module.unblackListDamageCause(cause);
                command.sender().sendMessage(command.locale().t("pipe.command.entity.damage_causes.removed").arg("cause", causeName).from("Entity").ct());
                return true;
            }

        }

    }

}
