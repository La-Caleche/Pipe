package fr.lacaleche.pipe.bukkit.tabs.features;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.*;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.nametag.NameTagController;
import fr.lacaleche.pipe.bukkit.tabs.nametag.PlayerNameTagImpl;
import fr.lacaleche.pipe.bukkit.tabs.nametag.interfaces.PlayerNameTag;
import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import fr.lacaleche.pipe.common.tasks.interfaces.Task;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageClass.*;

public class NameTagFeature extends AbstractTabFeature implements Loadable, Unloadable, Refreshable, JoinListener, QuitListener, PlayerSneakListener, EntityMoveListener, GameModeListener, NameTagLineChangedListener {

    private final Task task;

    public NameTagFeature() {
        task = Pipe.get().getTaskManager().newTask(new TaskBuilder().loop(true).callback(task -> {
            for (TabPlayer player : this.tab().getTabPlayers()) {
                player.update();

                for (TabPlayer viewer : this.tab().getTabPlayers()) {
                    if (player == viewer) continue;
                    this.tab().entityMove(viewer, player, false);
                }
            }
        }));
    }

    @Override
    public void refresh(TabPlayer tabPlayer) {
        for (TabPlayer viewer : this.tab().getTabPlayers()) {
            if (tabPlayer == viewer) continue;
            this.updateNameTagsFor(tabPlayer, viewer, true);
        }
    }

    @Override
    public void onEntityMove(TabPlayer viewer, TabPlayer player, boolean force) {
        if (player != null && viewer != null) {
            if (player.hasMoved() || force)
                player.getNameTag().teleport(viewer);
        }
    }

    @Override
    public void join(TabPlayer newPlayer) {
        for (TabPlayer tabPlayer : this.tab().getTabPlayers()) {
            if (tabPlayer == newPlayer) continue;
            this.updateNameTagsFor(tabPlayer, newPlayer, true);
            this.updateNameTagsFor(newPlayer, tabPlayer, true);
        }
    }

    @Override
    public void quit(TabPlayer tabPlayer) {
        List<Integer> lines = new ArrayList<>(tabPlayer.getNameTag().getLines().keySet());
        lines.forEach(tabPlayer.getNameTag()::removeLine);
        for (TabPlayer viewer : this.tab().getTabPlayers()) {
            if (viewer == tabPlayer) continue;
            this.updateNameTagsFor(tabPlayer, viewer, false);
        }
    }

    @Override
    public void onPlayerSneak(TabPlayer tabPlayer, boolean sneak) {
        tabPlayer.update();
        tabPlayer.getNameTag().setSneak(sneak);
    }

    @Override
    public int onGameModeChange(TabPlayer tabPlayer, UUID uuid, int newGameMode) {
        for (TabPlayer viewer : this.tab().getTabPlayers()) {
            if (viewer == tabPlayer) continue;
            this.updateNameTagsFor(tabPlayer, viewer, false);
        }
        return newGameMode;
    }

    @Override
    public void onLineChanged(TabPlayer viewer, TabPlayer player, int order) {
        this.updateNameTagsFor(player, viewer, true);
    }

    @Override
    public void load() {
        for (TabPlayer tabPlayer : this.tab().getTabPlayers()) {
            this.refresh(tabPlayer);
        }
    }

    @Override
    public void unload() {
        for (TabPlayer tabPlayer : this.tab().getTabPlayers()) {
            List<Integer> lines = new ArrayList<>(tabPlayer.getNameTag().getLines().keySet());
            lines.forEach(tabPlayer.getNameTag()::removeLine);
            for (TabPlayer viewer : this.tab().getTabPlayers()) {
                if (viewer == tabPlayer) continue;
                this.updateNameTagsFor(tabPlayer, viewer, false);
            }
        }

        this.task.stop();
    }

    private void updateNameTagsFor(TabPlayer tabPlayer, TabPlayer viewer, boolean updateText) {
        PlayerNameTag nameTag = tabPlayer.getNameTag();
        List<NameTagController> controllers = new ArrayList<>();
        if (nameTag.getLinesFor(viewer) != null && !nameTag.getLinesFor(viewer).isEmpty())
            controllers.addAll(nameTag.getLinesFor(viewer));

        for (NameTagController controller : controllers) {
            if (controller.needRemove() || !this.shouldSee(viewer, tabPlayer)) {
                controller.hide(viewer.getPlayer());
                controller.remove();
                nameTag.removeLineFor(viewer, controller.getOrder());

                continue ;
            }

            if (updateText) {
                controller.updateText();
            }
        }

        if (!this.shouldSee(viewer, tabPlayer)) return ;

        List<NameTagController> finalControllers = new ArrayList<>(controllers);
        Map<Integer, Object> finalLines = new HashMap<>(nameTag.getLines());
        finalLines.forEach((order, text) -> {
            if (finalControllers.stream().noneMatch(nameTagController -> nameTagController.getOrder() == order)) {
                NameTagController newLine = this.tab().getNmsManager().createEntity(NameTagController.class, new Location(viewer.getPlayer().getWorld(), 0, -100, 0));

                newLine.setTabPlayer(tabPlayer);
                newLine.setOrder(order);
                newLine.spawn();
                newLine.setInvisible(true);
                newLine.show(viewer.getPlayer());

                newLine.setText(text);
                newLine.teleport();

                nameTag.addLineFor(newLine, viewer);
            }
        });
    }

    private boolean shouldSee(TabPlayer viewer, TabPlayer tabPlayer) {
        if (viewer == tabPlayer) return false;
        if (viewer.getPlayer().getGameMode() != GameMode.SPECTATOR && tabPlayer.getPlayer().getGameMode() == GameMode.SPECTATOR) return false;
        return viewer.getPlayer().canSee(tabPlayer.getPlayer());
    }

}
