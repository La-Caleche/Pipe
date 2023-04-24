package fr.lacaleche.pipe.bukkit.tabs.features;

import fr.lacaleche.core.utils.commons.pairs.IPair;
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

public class NameTagFeature extends AbstractTabFeature implements Loadable, Unloadable, Refreshable, JoinListener, QuitListener, PlayerSneakListener, EntityMoveListener, GameModeListener, NameTagLineChangedListener, WorldChangeListener {

    private final Task task;

    public NameTagFeature() {
        task = Pipe.getBukkit().getTaskManager().newTask(new TaskBuilder().loop(true).callback(task -> {
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
            this.tab().entityMove(viewer, tabPlayer, true);
        }
    }

    @Override
    public void onEntityMove(TabPlayer viewer, TabPlayer player, boolean force) {
        if (player != null && viewer != null) {
            if (player.hasMoved() || force) {
                if (this.checkDistance(player, viewer)) {
                    if (player.getNameTag().isPositionLockedFor(viewer)) {
                        this.updateBothNameTags(player, viewer, true);
                        this.unlockPositionForBoth(player, viewer);
                    }

                    player.getNameTag().teleport(viewer);
                    viewer.getNameTag().teleport(player);
                } else {
                    this.updateBothNameTags(player, viewer, false);
                    this.lockPositionForBoth(player, viewer);
                }
            }
        }
    }

    @Override
    public void join(TabPlayer newPlayer) {
        for (TabPlayer tabPlayer : this.tab().getTabPlayers()) {
            if (tabPlayer == newPlayer) continue;
            this.updateBothNameTags(tabPlayer, newPlayer, true);
            this.tab().entityMove(tabPlayer, newPlayer, true);
        }
    }

    @Override
    public void quit(TabPlayer tabPlayer) {
        List<Integer> lines = new ArrayList<>(tabPlayer.getNameTag().getLines().keySet());
        lines.forEach(tabPlayer.getNameTag()::removeLine);
        for (TabPlayer viewer : this.tab().getTabPlayers()) {
            if (viewer == tabPlayer) continue;
            this.updateNameTagsFor(tabPlayer, viewer, false);
            viewer.getNameTag().removePlayer(tabPlayer);
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
    public void onWorldChange(TabPlayer tabPlayer, String worldName) {
        for (TabPlayer viewer : this.tab().getTabPlayers()) {
            if (viewer == tabPlayer) continue;
            this.updateBothNameTags(tabPlayer, viewer, false);
        }
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

        this.removeControllers(tabPlayer, viewer, nameTag, controllers, updateText);
        if (!this.shouldSee(viewer, tabPlayer)) return;
        this.summonControllersFor(tabPlayer, viewer, nameTag, controllers);
    }

    private void removeControllers(TabPlayer tabPlayer, TabPlayer viewer, PlayerNameTag nameTag, List<NameTagController> controllers, boolean updateText) {
        for (NameTagController controller : controllers) {
            if (controller.needRemove() || !this.shouldSee(viewer, tabPlayer)) {
                controller.hide(viewer.getPlayer());
                controller.remove();
                nameTag.removeLineFor(viewer, controller.getOrder());

                continue;
            }

            if (updateText) {
                controller.setText(nameTag.getLine(controller.getOrder()).getLeft());
            }
        }
    }

    private void summonControllersFor(TabPlayer tabPlayer, TabPlayer viewer, PlayerNameTag nameTag, List<NameTagController> controllers) {
        List<NameTagController> finalControllers = new ArrayList<>(controllers);
        Map<Integer, IPair<String, Boolean>> finalLines = new HashMap<>(nameTag.getLines());
        finalLines.forEach((order, text) -> {
            if (finalControllers.stream().noneMatch(nameTagController -> nameTagController.getOrder() == order)) {
                Location location = tabPlayer.getPlayer().getLocation();
                NameTagController newLine = this.tab().getNmsManager().createEntity(NameTagController.class, location.clone());

                newLine.setTabPlayer(tabPlayer);
                newLine.setOrder(order);
                newLine.spawn();
                newLine.setInvisible(true);
                newLine.show(viewer.getPlayer());

                newLine.setText(text.getLeft());
                newLine.teleport();

                nameTag.addLineFor(newLine, viewer);
            }
        });
    }

    private boolean checkDistance(TabPlayer tabPlayer, TabPlayer viewer) {
        return tabPlayer.getPlayer().getLocation().distance(viewer.getPlayer().getLocation()) <= 48;
    }

    private void updateBothNameTags(TabPlayer firstPlayer, TabPlayer secondPlayer, boolean updateText) {
        this.updateNameTagsFor(firstPlayer, secondPlayer, updateText);
        this.updateNameTagsFor(secondPlayer, firstPlayer, updateText);
    }

    private void lockPositionForBoth(TabPlayer firstPlayer, TabPlayer secondPlayer) {
        firstPlayer.getNameTag().lockPositionFor(secondPlayer);
        secondPlayer.getNameTag().lockPositionFor(firstPlayer);
    }

    void unlockPositionForBoth(TabPlayer firstPlayer, TabPlayer secondPlayer) {
        firstPlayer.getNameTag().unlockPositionFor(secondPlayer);
        secondPlayer.getNameTag().unlockPositionFor(firstPlayer);
    }

    private boolean shouldSee(TabPlayer viewer, TabPlayer tabPlayer) {
        if (viewer == tabPlayer) return false;
        if (!viewer.getPlayer().canSee(tabPlayer.getPlayer())) return false;
        if (viewer.getPlayer().getLocation().getWorld() != tabPlayer.getPlayer().getLocation().getWorld()) return false;
        if (viewer.getPlayer().getGameMode() != GameMode.SPECTATOR && tabPlayer.getPlayer().getGameMode() == GameMode.SPECTATOR)
            return false;
        return this.checkDistance(tabPlayer, viewer);
    }

}
