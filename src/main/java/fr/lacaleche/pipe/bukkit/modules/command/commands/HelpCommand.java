package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.utils.colors.Colors;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.arguments.RegisteredCommandArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.helper.command.HelperImpl;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.common.packets.HelpPacket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@MinecraftCommand(label = "help", aliases = {"?", "aide", "h"}, description = "Give help for every commands", arguments = {"command"})
public class HelpCommand {

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new RegisteredCommandArgument("command").setMandatory(false));
    }

    @CommandExecutor
    public boolean execute(CommandSender sender, Arguments arguments) {
        Locale locale = Pipe.get().getDefaultLocale();

        if (sender instanceof Player player) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            locale = client.getLocale();
        }

        if (arguments.blank("command")) {
            TextComponent.Builder builder = Component.text();
            builder.append(locale.t("command.helper.header").ct());
            builder.append(Component.newline());
            Locale finalLocale = locale;

            Pipe.get().getCommandManager().getNetworkCommands().forEach((app, commands) ->
                    commands.forEach(label -> {
                        if (label.startsWith("∅")) return;
                        builder.append(Component.text("  > ").color(TextColor.fromHexString(Colors.LC_MAIN_WHITE)).append(finalLocale.t("command.helper.click_to_run").arg("command", "help %s".formatted(label)).arg("label", StringUtils.capitalize(label)).ct()).append(Component.newline()));
                    })
            );
            builder.append(Component.newline());
            builder.append(locale.t("command.helper.footer").ct());

            sender.sendMessage(builder);
            return true;
        }

        if (!Pipe.get().getCommandManager().isRegisteredOnNetwork(arguments.get("command").getValue())) {
            sender.sendMessage(locale.t("command.helper.not_found").arg("label", arguments.get("command").getValue()).ct());
            return true;
        }

        Locale finalLocale1 = locale;
        HelpPacket packet = new HelpPacket(arguments.get("command").getValue(), locale, resolve -> {
            Component component = (Component) resolve;
            sender.sendMessage(component);
        }, reject -> {
            sender.sendMessage(finalLocale1.t("command.helper.not_found").arg("label", arguments.get("command").getValue()).ct());
        });
        CalecheCore.get().getPacketManager().publish(packet);

        return true;
    }

}
