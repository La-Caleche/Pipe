package fr.lacaleche.pipe.bukkit.modules.chat.listeners;

import fr.lacaleche.pipe.bukkit.modules.chat.renderers.PipeRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(new PipeRenderer(event.getPlayer()));
    }
}
