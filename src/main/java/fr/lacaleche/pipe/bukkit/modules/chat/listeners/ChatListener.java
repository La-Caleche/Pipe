package fr.lacaleche.pipe.bukkit.modules.chat.listeners;

import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.BukkitPipe;
import fr.lacaleche.pipe.bukkit.modules.chat.renderers.PipeRenderer;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(this.instanciateRendere(event.getPlayer()));
    }

    private ChatRenderer instanciateRendere(Audience player) {
        try {
            return Pipe.getBukkit().getChatRenderer().getConstructor(Audience.class).newInstance(player);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
            return new PipeRenderer(player);
        }
    }

}
