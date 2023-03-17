package fr.lacaleche.pipe.bukkit.modules.chat.renderers;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PipeRenderer extends AbstractRenderer {

    public PipeRenderer(Audience audience) {
        super((source, sourceDisplayName, message) -> {
            TextComponent.Builder textBuilder = Component.text();
            TextComponent.Builder hoverBuilder = Component.text();

            JavaPlugin plugin = Pipe.get().getPlugin();
            Server server = plugin.getServer();

            if (audience instanceof Player player) {
                Client client = Pipe.get().getClient(player.getUniqueId());

                textBuilder.append(sourceDisplayName.color(client.getRank().colorAsColor()));

                hoverBuilder.content("Rank : ").append(client.getRank().getColoredRankName(client.getLocale()));
                textBuilder.hoverEvent(hoverBuilder.asComponent().asHoverEvent());
            } else textBuilder.append(sourceDisplayName.color(NamedTextColor.GREEN));

            textBuilder.append(Component.text(" > "));
            textBuilder.append(message.color(NamedTextColor.GRAY));

            return textBuilder.build();
        });
    }

}
