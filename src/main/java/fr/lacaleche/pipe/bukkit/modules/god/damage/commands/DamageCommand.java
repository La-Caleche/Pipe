package fr.lacaleche.pipe.bukkit.modules.god.damage.commands;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.pipe.bukkit.modules.god.damage.DamageModule;
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

@MinecraftCommand(label = "damage", description = "pipe.command.damage.description")
public class DamageCommand {

    @CommandChild(label = "dcblacklist", description = "pipe.command.damage.dcblacklist.description")
    public static class BlockBlackList {

        @CommandExecutor
        public boolean execute(Command<CommandSender> command) {
            return true;
        }

        @CommandChild(label = "add", description = "pipe.command.damage.dcblacklist.add.description")
        public static class Add {

            @ArgumentsManager
            public void manager(ArgumentManager manager) {
                manager.addArgument(new CustomArgument("cause"));
            }

            @TabCompleter
            public void completer(Completer completer) {
                if (completer.index() == 1) {
                    List<EntityDamageEvent.DamageCause> causes = Arrays.stream(EntityDamageEvent.DamageCause.values()).toList();
                    List<EntityDamageEvent.DamageCause> blackList = CalecheCore.get().getCentralModuleManager().getModule(DamageModule.class).getBlackListDamageCauses();
                    completer.addAll(causes.stream().filter(cause -> !blackList.contains(cause)).map(EntityDamageEvent.DamageCause::name).collect(Collectors.toList()));
                }
            }

            @CommandExecutor
            public boolean execute(Command<CommandSender> command) {
                return true;
            }

        }

        @CommandChild(label = "remove", description = "pipe.command.damage.dcblacklist.remove.description")
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
                        List<EntityDamageEvent.DamageCause> blackList = CalecheCore.get().getCentralModuleManager().getModule(DamageModule.class).getBlackListDamageCauses();
                        completer.addAll(causes.stream().filter(blackList::contains).map(EntityDamageEvent.DamageCause::name).collect(Collectors.toList()));
                    }
                }
            }

            @CommandExecutor
            public boolean execute(Command<CommandSender> command) {
                return true;
            }

        }

    }

}
