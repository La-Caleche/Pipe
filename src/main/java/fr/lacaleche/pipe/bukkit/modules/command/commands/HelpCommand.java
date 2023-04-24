package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.core.utils.colors.Colors;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.command.arguments.BukkitRegisteredCommandArgument;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.helper.command.HelperImpl;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.ConsoleCommandSender;

import java.util.List;

@MinecraftCommand(label = "help", aliases = {"?", "aide", "h"}, description = "pipe.command.help.description", arguments = {"command"})
public class HelpCommand {

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitRegisteredCommandArgument("command").optional());
    }

    @CommandExecutor(executor = CommandExecutor.Executor.SERVER)
    public boolean execute(Command<ConsoleCommandSender> command) {
        CommandManager commandManager = Pipe.getBukkit().getCommandManager();
        List<String> commands = commandManager.getCommands().keySet().stream().map(s -> s.replace("∅", "")).toList();

        if (command.args().blank("command")) {
            TextComponent.Builder builder = Component.text();
            builder.append(command.locale().t("pipe.command.help.header").ct());
            builder.append(Component.newline());

            commands.stream().sorted().forEach((cmd) -> {
                if (cmd.startsWith("∅")) return;
                builder.append(Component.text("  > ").color(TextColor.fromHexString(Colors.LC_MAIN_WHITE)).append(command.locale().t("pipe.command.help.click_to_run").arg("command", "help %s".formatted(cmd)).arg("label", StringUtils.capitalize(cmd)).ct()).append(Component.newline()));
            });

            builder.append(Component.newline());
            builder.append(command.locale().t("pipe.command.help.footer").ct());

            command.sender().sendMessage(builder);

            return true;
        }
        String label = command.args().getString("command");

        if (!commands.contains(label)) {
            command.sender().sendMessage(command.locale().t("pipe.helper.command_not_found").arg("label", label).ct());
            return true;
        }

        if (commandManager.getCommand(label) != null) {
            TextComponent.Builder formatted = new HelperImpl(command.locale(), label).format(command.sender());
            command.sender().sendMessage(formatted);

            return true;
        }

        return true;
    }

}