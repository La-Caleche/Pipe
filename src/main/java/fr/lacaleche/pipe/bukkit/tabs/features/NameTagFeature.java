package fr.lacaleche.pipe.bukkit.tabs.features;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.*;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.nametag.NameTagController;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageClass.*;

public class NameTagFeature extends AbstractTabFeature implements Loadable, Unloadable, Refreshable, JoinListener, EntityMoveListener {

    private final Map<Integer, TabPlayer> entityIdMap = new ConcurrentHashMap<>();

    @Override
    public void refresh(TabPlayer tabPlayer) {
        for (TabPlayer viewer : this.tab().getTabPlayers()) {
            if (shouldSee(viewer, tabPlayer)) {
                this.updateShow(tabPlayer, viewer);
            }
        }

        this.updateShow(tabPlayer, tabPlayer);
    }

    @Override
    public void onEntityMove(TabPlayer receiver, int entityId) {
        TabPlayer tabPlayer = this.entityIdMap.get(entityId);
        if (tabPlayer != null) {
            tabPlayer.getNameTag().refresh();
        }

    }

    @Override
    public void join(TabPlayer tabPlayer) {
        this.entityIdMap.put(tabPlayer.getPlayer().getEntityId(), tabPlayer);
        this.refresh(tabPlayer);
    }

    @Override
    public void load() {
        for (TabPlayer tabPlayer : this.tab().getTabPlayers()) {
            this.entityIdMap.put(tabPlayer.getPlayer().getEntityId(), tabPlayer);
            this.refresh(tabPlayer);
        }
    }

    @Override
    public void unload() {
        for (TabPlayer tabPlayer : this.tab().getTabPlayers()) {
            tabPlayer.getNameTag().getLines().forEach(NameTagController::remove);
            for (TabPlayer viewer : this.tab().getTabPlayers()) {
                this.updateShow(tabPlayer, viewer);
            }

            this.updateShow(tabPlayer, tabPlayer);
        }
    }

    private void updateShow(TabPlayer tabPlayer, TabPlayer viewer) {
        tabPlayer.getNameTag().getLines().forEach(nameTagController -> {
            if (nameTagController.needRemove()) {
                nameTagController.hide(viewer.getPlayer());
                return ;
            }

            if (!nameTagController.canSee(viewer.getPlayer()))
                nameTagController.show(viewer.getPlayer());
        });
    }

    private boolean shouldSee(TabPlayer viewer, TabPlayer tabPlayer) {
        if (viewer == tabPlayer) return true;
        return viewer.getPlayer().canSee(tabPlayer.getPlayer());
    }

}
