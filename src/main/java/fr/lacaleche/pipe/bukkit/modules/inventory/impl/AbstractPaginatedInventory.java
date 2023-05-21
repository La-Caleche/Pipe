package fr.lacaleche.pipe.bukkit.modules.inventory.impl;

import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PaginatedInventory;
import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PipeInventory;
import fr.lacaleche.pipe.bukkit.modules.inventory.items.ItemBuilder;
import fr.lacaleche.pipe.bukkit.modules.inventory.items.anvilitems.StringItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class AbstractPaginatedInventory extends AbstractInventory implements PaginatedInventory {

    private int page = 0;
    private int totalSize;
    private int width;
    private int height;
    private int startAt;
    private int previousSlot;
    private int nextSlot;
    private int pageSlot;
    private int maxPages;
    private String filter;
    private boolean filterEnabled;
    private int filterSlot;
    private Material filterMaterial;

    public AbstractPaginatedInventory(Component title, Player player, InventoryStyle inventoryStyle, PipeInventory parent) {
        super(title, player, inventoryStyle, parent);

        this.startAt = 10;
        this.width = 7;
        this.height = 2;
        this.totalSize = 0;

        this.previousSlot = 27;
        this.nextSlot = 35;
        this.pageSlot = 4;

        this.filter = "";
        this.filterEnabled = false;
        this.filterSlot = -1;
        this.filterMaterial = Material.COMPASS;

        this.setMaxPages();
    }

    public AbstractPaginatedInventory(Component title, Player player, InventoryStyle inventoryStyle) {
        this(title, player, inventoryStyle, null);
    }

    @Override
    public void fill() {
        super.fill();

        for (int line = 0; line < this.height; line++) {
            int slot = this.startAt + line * 9;
            for (int j = slot; j < slot + this.width; j++) setItem(j, ItemBuilder.EMPTY.build());
        }

        double max = this.height * this.width;
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

            setItem(this.filterSlot, stringItem.buildAnvil(this, result -> {
                this.filter(result.getText());
                return stringItem.closeThenReopenParent(this);
            }).build(), (event) -> {
                this.hide();
                stringItem.getAnvil().open(this.getPlayer());
            });
        }
    }

    @Override
    public void applyFilter() {}

    @Override
    public void next(InventoryClickEvent inventoryClickEvent) {
        this.page++;
        this.refresh();
    }

    @Override
    public void previous(InventoryClickEvent inventoryClickEvent) {
        this.page--;
        this.refresh();
    }

    @Override
    public void renderPagination(boolean needPage, int pages) {
        setItem(this.pageSlot, new ItemBuilder(Material.NAME_TAG).name(this.getLocale().t("pipe.inventory.items.page").arg("page", page + 1).arg("max_page", pages).ct()).build());

        if (needPage) {
            if (page > 0)
                setItem(this.previousSlot, new ItemBuilder(Material.ARROW).name(this.getLocale().t("pipe.inventory.items.previous_page").ct()).build(), this::previous);
            if (page < pages - 1)
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
        this.height = height;
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

        double max = this.height * this.width;
        int maxPages = (int) Math.ceil(this.totalSize / max);

        if (maxPages == 0) maxPages = 1;
        this.maxPages = maxPages;
    }

}
