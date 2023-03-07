package fr.lacaleche.pipe.bukkit.modules.chat.listeners;

import com.velocitypowered.api.proxy.ProxyServer;
import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.chat.ChatModule;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.proxy.ProxyPlugin;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.setCancelled(true);

        CalecheCore.get().getCentralModuleManager().getModule(ChatModule.class).getChatManager().sendMessage(event.getPlayer(), event.message());
    }
}
