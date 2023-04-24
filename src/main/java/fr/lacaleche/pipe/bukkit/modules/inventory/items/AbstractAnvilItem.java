package fr.lacaleche.pipe.bukkit.modules.inventory.items;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PipeInventory;
import fr.lacaleche.pipe.bukkit.modules.inventory.items.interfaces.AnvilItem;
import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractAnvilItem implements AnvilItem {

    private AnvilGUI.Builder builder;

    public AbstractAnvilItem() {
        super();
    }

    @Override
    public ItemBuilder get() {
        return null;
    }

    @Override
    public AnvilGUI.Builder buildAnvil(PipeInventory parent, String title, String text, ItemBuilder left, Function<AnvilGUI.Completion, List<AnvilGUI.ResponseAction>> completeFunction) {
        this.builder = new AnvilGUI.Builder()
                .onComplete(completeFunction)
                .text(text)
                .title(title)
                .itemLeft(left.getItem())
                .onClose(player -> parent.showAndRefreshWithClose(1))
                .plugin(Pipe.getBukkit().getPlugin());
        return this.builder;
    }

    @Override
    public List<AnvilGUI.ResponseAction> closeThenReopenParent(PipeInventory parent) {
        Pipe.getBukkit().getTaskManager().newTask(new TaskBuilder().callback((task) -> parent.showAndRefresh()).startAfter(1));
        return Collections.singletonList(AnvilGUI.ResponseAction.close());
    }

    @Override
    public AnvilGUI.Builder getAnvil() {
        return this.builder;
    }

    @Override
    public boolean validateInt(String text) {
        return this.validateInt(text, 0, Integer.MAX_VALUE);
    }

    @Override
    public boolean validateInt(String text, int min, int max) {
        try {
            int amount = Integer.parseInt(text);
            return amount >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
