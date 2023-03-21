package fr.lacaleche.pipe.bukkit.modules.inventory.interfaces;

import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface PipeInventory {

    InventoryStyle getStyle();

    Inventory getInventory();

    Player getPlayer();

    boolean isVisible();

    boolean allowClose();

    void disallowClose();

    void show();

    void hide();

    void showAndRefresh();

    void showAndRefresh(int delay);

    void showAndRefreshWithClose(int delay);

    void fill();

    void clear();

    void refresh();

    void close();

    void unregister();

    void addItem(ItemStack item);

    void addItem(ItemStack item, Consumer<InventoryClickEvent> clickEvent);

    void setItem(int index, ItemStack item);

    void setItem(int index, ItemStack item, Consumer<InventoryClickEvent> clickEvent);

    void fillWith(ItemStack item);

    void trigItem(InventoryClickEvent event);

    PipeInventory getParent();

    void unregisterParents();

    Client getClient();

    Locale getLocale();

    enum InventoryStyle {

        HOPPER(0, InventoryType.HOPPER),
        _1X9(9),
        _2X9(2 * 9),
        _3X9(3 * 9),
        _4X9(4 * 9),
        _5X9(5 * 9),
        _6X9(6 * 9);

        private int size;
        private InventoryType type;

        InventoryStyle(int size, InventoryType type) {
            this.size = size;
            this.type = type;
        }

        InventoryStyle(int size) {
            this(size, InventoryType.CHEST);
        }

        public int getSize() {
            if (this.size == 0) return type.getDefaultSize();
            return size;
        }

        public InventoryType getType() {
            return type;
        }
    }

}
