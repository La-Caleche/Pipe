package fr.lacaleche.pipe.bukkit.modules.tab.listeners;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class TabPlayerListener implements Listener {

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        TabManager tabManager = Pipe.getBukkit().getTabManager();
        tabManager.onPlayerSneak(tabManager.getTabPlayer(event.getPlayer().getUniqueId()), event.isSneaking());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        TabManager tabManager = Pipe.getBukkit().getTabManager();
        tabManager.onWorldChange(tabManager.getTabPlayer(event.getPlayer().getUniqueId()), event.getPlayer().getWorld().getName());
    }

}
