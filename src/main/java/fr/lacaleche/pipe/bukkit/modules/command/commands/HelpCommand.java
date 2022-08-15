package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.utils.colors.Colors;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.arguments.RegisteredCommandArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import fr.lacaleche.pipe.common.packets.HelpPacket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@MinecraftCommand(label = "help", aliases = {"?", "aide", "h"}, description = "pipe.command.help.description", arguments = {"command"})
public class HelpCommand {

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new RegisteredCommandArgument("command").optional());
    }

    @CommandExecutor
    public boolean execute(Command<CommandSender> command) {
        if (command.args().blank("command")) {
            TextComponent.Builder builder = Component.text();
            builder.append(command.locale().t("pipe.command.help.header").ct());
            builder.append(Component.newline());

            Pipe.get().getCommandManager().getNetworkCommands().forEach((app, commands) ->
                    commands.forEach(label -> {
                        if (label.startsWith("∅")) return;
                        builder.append(Component.text("  > ").color(TextColor.fromHexString(Colors.LC_MAIN_WHITE)).append(command.locale().t("pipe.command.help.click_to_run").arg("command", "help %s".formatted(label)).arg("label", StringUtils.capitalize(label)).ct()).append(Component.newline()));
                    })
            );
            builder.append(Component.newline());
            builder.append(command.locale().t("pipe.command.help.footer").ct());

            command.sender().sendMessage(builder);
            return true;
        }

        if (!Pipe.get().getCommandManager().isRegisteredOnNetwork(command.args().getString("command"))) {
            command.sender().sendMessage(command.locale().t("pipe.helper.command_not_found").arg("label", command.args().getString("command")).ct());
            return true;
        }

        HelpPacket packet = new HelpPacket(command.args().getString("command"), command.locale(), resolve -> {
            Component component = (Component) resolve;
            command.sender().sendMessage(component);
        }, reject -> {
            command.sender().sendMessage(command.locale().t("pipe.helper.command_not_found").arg("label", command.args().getString("command")).ct());
        });
        CalecheCore.get().getPacketManager().publish(packet);

        return true;
    }

}