package fr.lacaleche.pipe.bukkit.modules.inventory.impl;

import de.tr7zw.nbtapi.NBT;
import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.inventory.InventoryManager;
import fr.lacaleche.pipe.bukkit.modules.inventory.InventoryModule;
import fr.lacaleche.pipe.bukkit.modules.inventory.events.InventoryFillEvent;
import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PipeInventory;
import fr.lacaleche.pipe.bukkit.modules.inventory.items.ItemBuilder;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class AbstractInventory implements PipeInventory {

    private Player player;
    private Client client;

    private Inventory inventory;
    private PipeInventory parent;

    private InventoryStyle inventoryStyle;

    private boolean visible = false;
    private boolean allowClose = true;
    private Map<UUID, Consumer<InventoryClickEvent>> clickEvents;

    private InventoryManager inventoryManager;

    public AbstractInventory(Component title, Player player, InventoryStyle inventoryStyle, PipeInventory parent) {
        if (!Core.get().getCentralModuleManager().moduleEnabled(InventoryModule.class)) throw new IllegalStateException("Inventory module is not enabled !");

        this.inventoryManager = Core.get().getCentralModuleManager().getModule(InventoryModule.class).getInventoryManager();

        this.player = player;
        this.client = Pipe.get().getClient(player.getUniqueId());
        this.inventoryStyle = inventoryStyle;
        this.parent = parent;

        this.clickEvents = new HashMap<>();

        final Plugin plugin = Pipe.get().getPlugin();

        if (this.getStyle().getType() == InventoryType.CHEST) this.inventory = plugin.getServer().createInventory(null, getStyle().getSize(), title);
        else this.inventory = plugin.getServer().createInventory(null, getStyle().getType(), title);

        this.inventoryManager.registerInventory(this);
    }

    public AbstractInventory(Component title, Player player, InventoryStyle inventoryStyle) {
        this(title, player, inventoryStyle, null);
    }

    @Override
    public void disallowClose() {
        this.allowClose = false;
    }

    @Override
    public boolean allowClose() {
        return this.allowClose;
    }

    @Override
    public void show() {
        this.visible = true;
        this.getPlayer().openInventory(this.getInventory());
    }

    @Override
    public void hide() {
        this.visible = false;
        this.getPlayer().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
    }

    @Override
    public void showAndRefresh() {
        this.show();
        this.refresh();
    }

    @Override
    public void showAndRefresh(int delay) {
        this.getPlayer().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        Pipe.get().getTaskManager().newTask(new TaskBuilder().callback((task) -> this.showAndRefresh()).startAfter(delay));
    }

    @Override
    public void close() {
        if (this.getPlayer() != null && this.getPlayer().getInventory() != null) this.getPlayer().closeInventory(InventoryCloseEvent.Reason.UNLOADED);
        this.unregister();
    }

    @Override
    public void unregister() {
        this.visible = false;
        this.inventory.clear();
        this.inventory = null;

        this.inventoryManager.unregisterInventory(this);
    }

    @Override
    public void clear() {
        this.getInventory().clear();
    }

    @Override
    public void refresh() {
        this.clear();
        InventoryFillEvent fillEvent = new InventoryFillEvent(this);
        fillEvent.call();

        if (!fillEvent.isCancelled()) fill();
    }

    @Override
    public void unregisterParents() {
        if (this.getParent() == null) return;
        this.getParent().unregister();
        this.getParent().unregisterParents();
    }

    @Override
    public void addItem(ItemStack item) {
        this.addItem(item, null);
    }

    @Override
    public void addItem(ItemStack item, Consumer<InventoryClickEvent> clickEvent) {
        if (item == null || this.inventory == null) return;

        UUID uuid = UUID.randomUUID();
        NBT.modify(item, readWriteItemNBT -> {
            readWriteItemNBT.setString("clickActionID", uuid.toString());
        });

        this.inventory.addItem(item);
        this.clickEvents.put(uuid, clickEvent);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        this.setItem(index, item, null);
    }

    @Override
    public void setItem(int index, ItemStack item, Consumer<InventoryClickEvent> clickEvent) {
        if (index < 0 || item == null || this.inventory == null) return;

        if (clickEvent != null && item.getAmount() > 0 && item.getType() != Material.AIR) {
            UUID uuid = UUID.randomUUID();
            NBT.modify(item, readWriteItemNBT -> {
                readWriteItemNBT.setString("clickActionID", uuid.toString());
            });

            this.clickEvents.put(uuid, clickEvent);
        }

        this.inventory.setItem(index, item);
    }

    @Override
    public void trigItem(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getClickedInventory() != getInventory() || event.getCurrentItem() == null) return;

        String stringUUID = NBT.get(event.getCurrentItem(), readableNBT -> readableNBT.getString("clickActionID") );
        if (stringUUID.isBlank()) return;

        UUID uuid;
        try {
            uuid = UUID.fromString(stringUUID);
        } catch (IllegalArgumentException ignored) { return; }

        Consumer<InventoryClickEvent> clickEvent = this.clickEvents.get(uuid);
        if (clickEvent == null) return;

        clickEvent.accept(event);
    }

    @Override
    public void fillWith(ItemStack item) {
        for (int i = 0; i < getStyle().getSize(); i++)
            setItem(i, item);
    }

    @Override
    public PipeInventory getParent() {
        return parent;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public InventoryStyle getStyle() {
        return this.inventoryStyle;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public Locale getLocale() {
        return this.getClient().getLocale();
    }

    protected void defaultFill(Material fillWith, int closeSlot, int backSlot) {
        this.defaultFill(new ItemBuilder(fillWith), closeSlot, backSlot);
    }

    protected void defaultFill(ItemBuilder builder, int closeSlot, int backSlot) {
        if (builder != null) fillWith(builder.build());

        if (closeSlot > -1)
            setItem(closeSlot, new ItemBuilder(Material.BARRIER).name(this.getLocale().t("pipe.inventory.items.quit").ct()).build(), (event) -> {
                this.close();
            });

        if (backSlot > -1 && this.getParent() != null)
            setItem(backSlot, new ItemBuilder(Material.ARROW).name(this.getLocale().t("pipe.inventory.items.back").ct()).build(), (event) -> {
                this.getParent().showAndRefresh();
                this.unregister();
            });
    }

}
