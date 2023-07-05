package fr.lacaleche.pipe.bukkit.modules.inventory.impl;

import de.tr7zw.nbtapi.NBT;
import fr.lacaleche.core.Core;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.BukkitPipe;
import fr.lacaleche.pipe.bukkit.modules.inventory.InventoryManager;
import fr.lacaleche.pipe.bukkit.modules.inventory.InventoryModule;
import fr.lacaleche.pipe.bukkit.modules.inventory.events.InventoryFillEvent;
import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PipeInventory;
import fr.lacaleche.pipe.bukkit.modules.inventory.items.ItemBuilder;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSModule;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.IInventory;
import net.minecraft.world.inventory.Containers;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor.PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor.PACKET_PLAY_OUT_OPEN_SCREEN_CONSTRUCTOR;

public abstract class AbstractInventory implements PipeInventory {

    private Player player;
    private Client client;

    private Inventory inventory;
    private PipeInventory parent;

    private InventoryStyle inventoryStyle;

    private boolean visible = false;
    private boolean allowClose = true;
    private boolean allowInteract = false;
    private int backSlot;
    private int closeSlot;
    private Map<UUID, Consumer<InventoryClickEvent>> clickEvents;

    private Material closeMaterial = Material.BARRIER;
    private Material backMaterial = Material.ARROW;
    private ItemBuilder background = ItemBuilder.EMPTY;

    private InventoryManager inventoryManager;

    public AbstractInventory(Player player, InventoryStyle inventoryStyle, PipeInventory parent) {
        if (!Core.get().getCentralModuleManager().moduleEnabled(InventoryModule.class)) throw new IllegalStateException("Inventory module is not enabled !");

        this.inventoryManager = Core.getModule(InventoryModule.class).getInventoryManager();

        this.player = player;
        this.client = Pipe.getBukkit().getClient(player.getUniqueId());
        this.inventoryStyle = inventoryStyle;
        this.parent = parent;

        this.clickEvents = new HashMap<>();

        this.backSlot = -1;
        this.closeSlot = -1;
    }

    public AbstractInventory(Player player, InventoryStyle inventoryStyle) {
        this(player, inventoryStyle, null);
    }

    @Override
    public void buildContainer(Component title) {
        final Plugin plugin = Pipe.getBukkit().getPlugin();

        if (this.getStyle().getType() == InventoryType.CHEST) this.inventory = plugin.getServer().createInventory(null, getStyle().getSize(), title);
        else this.inventory = plugin.getServer().createInventory(null, getStyle().getType(), title);

        this.inventoryManager.registerInventory(this);
    }

    @Override
    public void setTitle(Component title) {
        BukkitPipe pipe = Pipe.getBukkit();
        NMSManager nmsManager = Core.getModule(NMSModule.class).getNmsManager();
        IChatBaseComponent vanillaComponent = nmsManager.getStorage().construct(StorageConstructor.ADVENTURE_COMPONENT_CONSTRUCTOR, title);

        EntityPlayer entityPlayer = nmsManager.getPlayerHandle(this.getPlayer());
        int containerId = entityPlayer.bP.j;
        Containers<?> menuType = entityPlayer.bP.a();

        Object packet = nmsManager.getStorage().construct(PACKET_PLAY_OUT_OPEN_SCREEN_CONSTRUCTOR, containerId, menuType, vanillaComponent);
        nmsManager.sendPacket(this.getPlayer(), packet);
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
        Pipe.getBukkit().getTaskManager().newTask(taskBuilder -> taskBuilder.run((task) -> this.showAndRefresh()).startAfter(delay));
    }

    @Override
    public void showAndRefreshWithClose(int delay) {
        this.getPlayer().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        Pipe.getBukkit().getTaskManager().newTask(taskBuilder -> taskBuilder.run((task) -> this.showAndRefresh()).startAfter(delay));
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

        this.inventoryManager.unregisterInventory(this);

        this.inventory = null;
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
        if (event.getClickedInventory() != event.getWhoClicked().getInventory() || !allowInteract) event.setCancelled(true);
        if (event.getClickedInventory() != getInventory() || event.getCurrentItem() == null) return;

        String stringUUID = NBT.get(event.getCurrentItem(), readableNBT -> readableNBT.getString("clickActionID") );
        if (stringUUID.isBlank()) return;

        UUID uuid;
        try {
            uuid = UUID.fromString(stringUUID);
        } catch (IllegalArgumentException ignored) { return; }

        Consumer<InventoryClickEvent> clickEvent = this.clickEvents.get(uuid);
        if (clickEvent == null) return;

        event.setCancelled(true);
        clickEvent.accept(event);
    }

    @Override
    public void dragItem(InventoryDragEvent event) {
        if (event.getInventory() != getInventory() || !allowInteract) event.setResult(Event.Result.DENY);
    }

    @Override
    public void fillWith(ItemStack item) {
        for (int i = 0; i < getStyle().getSize(); i++)
            setItem(i, item);
    }

    @Override
    public void fillLine(int line, ItemStack item) {
        int maxLines = getStyle().getSize() / 9;
        if (line < 0 || line > maxLines) return;

        int start = line * 9;
        int end = start + 9;

        for (int i = start; i < end; i++)
            setItem(i, item);
    }

    @Override
    public void fillColumn(int column, ItemStack item) {
        int maxColumns = 9;
        if (column < 0 || column > maxColumns) return;

        for (int i = column; i < getStyle().getSize(); i += 9)
            setItem(i, item);
    }

    @Override
    public void fillLine(int line, int from, int size, ItemStack item) {
        int maxLines = getStyle().getSize() / 9;
        if (line < 0 || line > maxLines) return;

        int start = line * 9 + from;
        int end = start + size;

        for (int i = start; i < end; i++)
            setItem(i, item);
    }

    @Override
    public void fillColumn(int column, int from, int size, ItemStack item) {
        int maxColumns = 9;
        if (column < 0 || column > maxColumns) return;

        int start = column + (from * 9);
        int end = start + (size * 9);

        for (int i = start; i < end; i += 9)
            setItem(i, item);
    }

    @Override
    public void fillLine(int line, int size, ItemStack item) {
        fillLine(line, 0, size, item);
    }

    @Override
    public void fillColumn(int column, int size, ItemStack item) {
        fillColumn(column, 0, size, item);
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

    @Override
    public void setBackSlot(int backSlot) {
        this.backSlot = backSlot;
    }

    @Override
    public void setCloseSlot(int closeSlot) {
        this.closeSlot = closeSlot;
    }

    @Override
    public void setBackMaterial(Material backMaterial) {
        this.backMaterial = backMaterial;
    }

    @Override
    public void setCloseMaterial(Material closeMaterial) {
        this.closeMaterial = closeMaterial;
    }

    @Override
    public void setBackground(Material material) {
        this.setBackground(new ItemBuilder(material).name(""));
    }

    @Override
    public void setBackground(ItemBuilder itemBuilder) {
        this.background = itemBuilder;
    }

    @Override
    public void allowInteract(boolean allow) {
        this.allowInteract = allow;
    }

    @Override
    public boolean allowInteract() {
        return this.allowInteract;
    }

    @Override
    public void fill() {
        this.drawBackground();
        this.drawBackSlot();
        this.drawCloseButton();
    }

    protected void drawBackground() {
        if (this.background == null || this.background.getItem().getType() == Material.AIR) return ;
        this.fillWith(this.background.build());
    }

    protected void drawCloseButton() {
        if (this.closeSlot < 0) return ;
        setItem(this.closeSlot, new ItemBuilder(this.closeMaterial).name(this.getLocale().t("pipe.inventory.items.quit").ct()).build(), (event) -> {
            this.close();
        });
    }

    protected void drawBackSlot() {
        if (this.backSlot < 0) return;
        setItem(this.backSlot, new ItemBuilder(this.backMaterial).name(this.getLocale().t("pipe.inventory.items.back").ct()).build(), (event) -> {
            this.getParent().showAndRefresh();
            this.unregister();
        });
    }

}
