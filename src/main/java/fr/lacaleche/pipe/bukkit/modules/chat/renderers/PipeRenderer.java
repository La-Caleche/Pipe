package fr.lacaleche.pipe.bukkit.modules.chat.renderers;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.BukkitPipe;
import fr.lacaleche.pipe.common.clients.Client;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PipeRenderer extends AbstractRenderer {

    public PipeRenderer(Audience audience) {
        super((source, sourceDisplayName, message) -> {
            BukkitPipe pipe = Pipe.getBukkit();

            Plugin plugin = pipe.getPlugin();
            Server server = plugin.getServer();

            Component parsedMessage = pipe.text().deserialize(pipe.text().serialize(message).replace("\\", "")).colorIfAbsent(NamedTextColor.GRAY);

            TextComponent.Builder textBuilder = Component.text();
            TextComponent.Builder hoverBuilder = Component.text();

            if (audience instanceof Player player) {
                Client client = pipe.getClient(player.getUniqueId());

                textBuilder.append(sourceDisplayName.color(client.getRank().colorAsColor()));

                hoverBuilder.content("Rank : ").append(client.getRank().getColoredRankName(client.getLocale()));
                textBuilder.hoverEvent(hoverBuilder.asComponent().asHoverEvent());
            } else textBuilder.append(sourceDisplayName.color(NamedTextColor.GREEN));

            textBuilder.append(Component.text(" > "));

            if (source instanceof Player player) {
                Client client = pipe.getClient(player.getUniqueId());
                if (client.hasPermissionOrLevel("global.chat.bypass", 100)) textBuilder.append(parsedMessage);
                else textBuilder.append(message.color(NamedTextColor.GRAY));
            } else textBuilder.append(parsedMessage);

            return textBuilder.build();
        });
    }

}
