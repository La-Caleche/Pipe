package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import org.bukkit.command.CommandSender;

@MinecraftCommand(label = "pipedebug", description = "pipe.command.pipedebug.description")
public class PipeDebugCommand {

    @CommandExecutor(minPermLevel = 100, permissions = "pipe.command.pipedebug")
    public boolean execute(Command<CommandSender> command) {
        Logger.info("In dev: " + Core.get().inDev());

        command.sender().sendMessage(command.locale().t("pipe.command.pipedebug.informations").arg("appname", Core.get().getAppName()).from("System").ct());
        command.sender().sendMessage(command.locale().ct("pipe.command.pipedebug.dev_status.enabled", "pipe.command.pipedebug.dev_status.disabled", Core.get().inDev()).from("System").ct());
        command.sender().sendMessage(command.locale().ct("pipe.command.pipedebug.debug_status.enabled", "pipe.command.pipedebug.debug_status.disabled", Core.get().debugEnabled()).from("System").ct());

        return true;
    }

    @CommandChild(label = "switch", description = "pipe.command.pipedebug.switch.description")
    public static class Switch {

        @CommandExecutor(minPermLevel = 100, permissions = "pipe.command.pipedebug.switch")
        public boolean execute(Command<CommandSender> command) {
            Core.get().setDebugEnabled(!Core.get().debugEnabled());
            command.sender().sendMessage(command.locale().ct("pipe.command.pipedebug.debug_status.enabled", "pipe.command.pipedebug.debug_status.disabled", Core.get().debugEnabled()).from("System").ct());
            return true;
        }

    }

}
