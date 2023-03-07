package fr.lacaleche.pipe.bukkit.modules.chat;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatManager {

    public void sendMessage(Audience audience, Component message) {
        TextComponent.Builder textBuilder = Component.text();
        TextComponent.Builder hoverBuilder = Component.text();

        JavaPlugin plugin = Pipe.get().getPlugin();
        Server server = plugin.getServer();

        if (audience instanceof Player player) {
            Client client = Pipe.get().getClient(player.getUniqueId());

            textBuilder.append(client.getRank().colorize(player.getName()));
            hoverBuilder.content("Rank : ").append(client.getRank().getColoredRankName(client.getLocale()));
            textBuilder.hoverEvent(hoverBuilder.asComponent().asHoverEvent());
        } else
            textBuilder.append(Component.text("Console", NamedTextColor.GREEN));

        textBuilder.append(Component.text(" > "));
        textBuilder.append(message.color(NamedTextColor.GRAY));
        server.getOnlinePlayers().forEach(p -> {
            p.sendMessage(textBuilder.asComponent());
        });
        server.getConsoleSender().sendMessage(textBuilder.asComponent());
    }
}
