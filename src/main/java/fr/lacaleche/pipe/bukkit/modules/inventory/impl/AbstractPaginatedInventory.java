package fr.lacaleche.pipe.bukkit.modules.inventory.impl;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PaginatedInventory;
import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PipeInventory;
import fr.lacaleche.pipe.bukkit.modules.inventory.items.ItemBuilder;
import fr.lacaleche.pipe.bukkit.modules.inventory.items.anvilitems.StringItem;
import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryView;

import java.util.Collections;
import java.util.Set;

public abstract class AbstractPaginatedInventory extends AbstractInventory implements PaginatedInventory {

    private int page = 0;
    private int totalSize;
    private int width;
    private int height;
    private int startAt;
    private int previousSlot;
    private int nextSlot;
    private int pageSlot;
    private int scrollOffset;
    private boolean alwaysRenderPagination;
    private int maxPages;
    private String filter;
    private boolean filterEnabled;
    private int filterSlot;
    private Material filterMaterial;

    public AbstractPaginatedInventory(Player player, InventoryStyle inventoryStyle, PipeInventory parent) {
        super(player, inventoryStyle, parent);

        this.startAt = 10;
        this.width = 7;
        this.height = 2;
        this.totalSize = 0;
        this.scrollOffset = this.height;

        this.previousSlot = 27;
        this.nextSlot = 35;
        this.pageSlot = 4;
        this.alwaysRenderPagination = false;

        this.filter = "";
        this.filterEnabled = false;
        this.filterSlot = -1;
        this.filterMaterial = Material.COMPASS;

        this.setMaxPages();
    }

    public AbstractPaginatedInventory(Player player, InventoryStyle inventoryStyle) {
        this(player, inventoryStyle, null);
    }

    @Override
    public void fill() {
        super.fill();

        for (int line = 0; line < this.height; line++) {
            int slot = this.startAt + line * 9;
            for (int j = slot; j < slot + this.width; j++) setItem(j, ItemBuilder.EMPTY.build());
        }

        double max = this.scrollOffset * this.width;
        int itemsIndex = (int) Math.ceil(max * page);

        for (int line = 0; line < this.height; line++) {
            if (itemsIndex == this.totalSize) break;
            int startAt = this.startAt;

            int slot = startAt + line * 9;
            for (int j = slot; j < slot + this.width; j++) {
                if (itemsIndex == this.totalSize) break;
                this.renderSlot(itemsIndex, j);
                itemsIndex++;
            }
        }

        this.renderPagination(max < this.totalSize, this.maxPages);

        if (this.filterEnabled() && this.filterSlot > -1) {
            StringItem stringItem = new StringItem(new ItemBuilder(this.filterMaterial).name(this.getLocale().t("pipe.inventory.items.filter").ct()));
            if (!this.getFilter().isBlank()) {
                stringItem.get().addLine(Component.empty()).addLine(this.getLocale().t("pipe.inventory.items.filter.active-filter").arg("filter", this.getFilter()).ct());
            }
            stringItem.get().addLine(Component.empty()).addLine(this.getLocale().t("pipe.inventory.items.filter.description").ct());

            setItem(this.filterSlot, stringItem.buildAnvil(this, (slot, stateSnapshot) -> {
                if (slot != AnvilGUI.Slot.OUTPUT)
                    return Collections.emptyList();

                this.filter(stateSnapshot.getText());
                return stringItem.closeThenReopenParent(this);
            }).build(), (event) -> {
                if (event.getClick() == ClickType.RIGHT) {
                    this.filter("");
                } else {
                    this.hide();
                    stringItem.getAnvil().open(this.getPlayer());
                }
            });
        }
    }

    @Override
    public void dragItem(InventoryDragEvent event) {
        super.dragItem(event);

        InventoryView view = event.getView();
        for (int rawSlot : event.getRawSlots()) {
            if (rawSlot < view.getTopInventory().getSize()) {
                event.setResult(Event.Result.DENY);
                return;
            }
        }
    }

    @Override
    public boolean insidePaginatedView(int slot) {
        if(slot < this.startAt || slot >= this.startAt + this.height * 9) {
            return false;
        }
        return (slot - this.startAt) % 9 < this.width;
    }

    protected boolean dragInTopInventory(InventoryDragEvent event) {
        InventoryView view = event.getView();
        for (int rawSlot : event.getRawSlots()) {
            if (rawSlot < view.getTopInventory().getSize()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void applyFilter() {}

    @Override
    public void next(InventoryClickEvent inventoryClickEvent) {
        if (this.page == this.maxPages - 1) return ;
        this.page++;
        this.refresh();
    }

    @Override
    public void previous(InventoryClickEvent inventoryClickEvent) {
        if (this.page == 0) return ;
        this.page--;
        this.refresh();
    }

    @Override
    public void alwaysRenderPagination(boolean alwaysRenderPagination) {
        this.alwaysRenderPagination = alwaysRenderPagination;
    }

    @Override
    public void renderPagination(boolean needPage, int pages) {
        setItem(this.pageSlot, new ItemBuilder(Material.NAME_TAG).name(this.getLocale().t("pipe.inventory.items.page").arg("page", page + 1).arg("max_page", pages).ct()).build());

        if (needPage || this.alwaysRenderPagination) {
            if (page > 0 || this.alwaysRenderPagination)
                setItem(this.previousSlot, new ItemBuilder(Material.ARROW).name(this.getLocale().t("pipe.inventory.items.previous_page").ct()).build(), this::previous);
            if (page < pages - 1 || this.alwaysRenderPagination)
                setItem(this.nextSlot, new ItemBuilder(Material.ARROW).name(this.getLocale().t("pipe.inventory.items.next_page").ct()).build(), this::next);
        }
    }

    @Override
    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
        this.setMaxPages();
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
        this.setMaxPages();
    }

    @Override
    public void setHeight(int height) {
        this.setHeight(height, true);
    }

    @Override
    public void setHeight(int height, boolean updateOffset) {
        this.height = height;
        if (updateOffset) this.scrollOffset = height;
        this.setMaxPages();
    }

    @Override
    public void setScrollOffset(int scrollOffset) {
        this.scrollOffset = scrollOffset;
        this.setMaxPages();
    }

    @Override
    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    @Override
    public void setPreviousSlot(int slot) {
        this.previousSlot = slot;
    }

    @Override
    public void setNextSlot(int slot) {
        this.nextSlot = slot;
    }

    @Override
    public void setPageSlot(int slot) {
        this.pageSlot = slot;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public int getPage() {
        return this.page;
    }

    @Override
    public int getTotalSize() {
        return this.totalSize;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getStartAt() {
        return this.startAt;
    }

    @Override
    public int getPreviousSlot() {
        return this.previousSlot;
    }

    @Override
    public int getNextSlot() {
        return this.nextSlot;
    }

    @Override
    public int getPageSlot() {
        return this.pageSlot;
    }

    @Override
    public int getMaxPages() {
        return maxPages;
    }

    @Override
    public int getScrollOffset() {
        return scrollOffset;
    }

    @Override
    public void filter(String filter) {
        this.filter = filter;
        this.applyFilter();
    }

    @Override
    public void enableFiltering() {
        this.filterEnabled = true;
    }

    @Override
    public void disableFiltering() {
        this.filterEnabled = false;
    }

    @Override
    public boolean filterEnabled() {
        return this.filterEnabled;
    }

    @Override
    public String getFilter() {
        return this.filter;
    }

    @Override
    public void setFilterSlot(int filterSlot) {
        this.filterSlot = filterSlot;
    }

    @Override
    public void setFilterMaterial(Material filterMaterial) {
        this.filterMaterial = filterMaterial;
    }

    private void setMaxPages() {
        if (this.totalSize == 0) {
            this.maxPages = 1;
            return;
        }

        double max = this.scrollOffset * this.width;
        int maxPages = (int) Math.ceil(this.totalSize / max);

        if (maxPages == 0) maxPages = 1;
        this.maxPages = maxPages;
    }

}
