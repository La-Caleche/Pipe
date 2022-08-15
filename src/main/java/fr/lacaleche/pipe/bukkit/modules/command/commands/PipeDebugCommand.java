package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@MinecraftCommand(label = "pipedebug", description = "pipe.command.pipedebug.description")
public class PipeDebugCommand {

    @CommandExecutor
    public boolean execute(Command<CommandSender> command) {
        Logger.info("In dev: " + CalecheCore.get().inDev());

        command.sender().sendMessage(command.locale().t("pipe.command.pipedebug.informations").arg("appname", CalecheCore.get().getAppName()).from("System").ct());
        command.sender().sendMessage(command.locale().ct("pipe.command.pipedebug.dev_status.enabled", "pipe.command.pipedebug.dev_status.disabled", CalecheCore.get().inDev()).from("System").ct());
        command.sender().sendMessage(command.locale().ct("pipe.command.pipedebug.debug_status.enabled", "pipe.command.pipedebug.debug_status.disabled", CalecheCore.get().debugEnabled()).from("System").ct());

        return true;
    }

    @CommandChild(label = "switch", description = "pipe.command.pipedebug.switch.description")
    public static class Switch {

        @CommandExecutor
        public boolean execute(Command<CommandSender> command) {


            CalecheCore.get().setDebugEnabled(!CalecheCore.get().debugEnabled());
            command.sender().sendMessage(command.locale().ct("pipe.command.pipedebug.debug_status.enabled", "pipe.command.pipedebug.debug_status.disabled", CalecheCore.get().debugEnabled()).from("System").ct());
            return true;
        }

    }

}
