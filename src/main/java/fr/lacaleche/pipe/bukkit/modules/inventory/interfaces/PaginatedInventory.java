package fr.lacaleche.pipe.bukkit.modules.inventory.interfaces;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface PaginatedInventory {

    void next(InventoryClickEvent inventoryClickEvent);

    void previous(InventoryClickEvent inventoryClickEvent);

    void renderPagination(boolean needPage, int pages);

    void renderSlot(int index, int slot);

    void setTotalSize(int totalSize);

    void setWidth(int width);

    void setHeight(int height);

    void setStartAt(int startAt);

    void setPreviousSlot(int slot);

    void setNextSlot(int slot);

    void setPageSlot(int slot);

    int getPage();

    int getTotalSize();

    int getWidth();

    int getHeight();

    int getStartAt();

    int getPreviousSlot();

    int getNextSlot();

    int getPageSlot();

    int getMaxPages();

}
