package fr.lacaleche.pipe.bukkit.modules.inventory.interfaces;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface PaginatedInventory {

    void next(InventoryClickEvent inventoryClickEvent);

    void previous(InventoryClickEvent inventoryClickEvent);

    void renderPagination(boolean needPage, int pages);

    void renderSlot(int index, int slot);

    void applyFilter();

    void setTotalSize(int totalSize);

    void setWidth(int width);

    void setHeight(int height);

    void setHeight(int height, boolean updateOffset);

    void setScrollOffset(int offset);

    void setStartAt(int startAt);

    void setPreviousSlot(int slot);

    void setNextSlot(int slot);

    void setPageSlot(int slot);

    void alwaysRenderPagination(boolean alwaysRenderPagination);

    void filter(String filter);

    void enableFiltering();

    void disableFiltering();

    void setFilterSlot(int slot);

    void setFilterMaterial(Material material);

    void setPage(int page);

    int getPage();

    int getTotalSize();

    int getWidth();

    int getHeight();

    int getStartAt();

    int getPreviousSlot();

    int getNextSlot();

    int getPageSlot();

    int getMaxPages();

    int getScrollOffset();

    boolean filterEnabled();

    String getFilter();

}
