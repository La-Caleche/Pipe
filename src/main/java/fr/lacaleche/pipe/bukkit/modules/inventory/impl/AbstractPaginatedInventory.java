package fr.lacaleche.pipe.bukkit.modules.inventory.impl;

import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PaginatedInventory;
import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PipeInventory;
import fr.lacaleche.pipe.bukkit.modules.inventory.items.ItemBuilder;
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

    public AbstractPaginatedInventory(Component title, Player player, InventoryStyle inventoryStyle, PipeInventory parent) {
        super(title, player, inventoryStyle, parent);

        this.startAt = 10;
        this.width = 7;
        this.height = 2;
        this.totalSize = 0;

        this.previousSlot = 27;
        this.nextSlot = 35;
        this.pageSlot = 4;
    }

    public AbstractPaginatedInventory(Component title, Player player, InventoryStyle inventoryStyle) {
        this(title, player, inventoryStyle, null);
    }

    @Override
    public void fill() {
        for (int line = 0; line < this.height; line++) {
            int slot = this.startAt + line * 9;
            for (int j = slot; j < slot + this.width; j++) setItem(j, ItemBuilder.EMPTY.build());
        }

        double max = this.height * this.width;
        boolean needPage = max < this.totalSize;
        int pages = (int) Math.ceil(this.totalSize / max);
        int i = (int) Math.ceil(max * page);

        for (int line = 0; line < this.height; line++) {
            if (i == this.totalSize) break;
            int slot = this.startAt + line * 9;
            for (int j = slot; j < slot + this.width; j++) {
                if (i == this.totalSize) break;
                this.renderSlot(i, j);
                i++;
            }
        }

        this.renderPagination(needPage, pages == 0 ? pages + 1 : pages);
    }

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
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
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
}
