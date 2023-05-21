package fr.lacaleche.pipe.bukkit.modules.inventory.items.interfaces;

import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PipeInventory;
import fr.lacaleche.pipe.bukkit.modules.inventory.items.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;

import java.util.List;
import java.util.function.Function;

public interface AnvilItem extends InventoryItem {

    AnvilGUI.Builder buildAnvil(PipeInventory parent, String title, Component componentText, ItemBuilder left, Function<AnvilGUI.Completion, List<AnvilGUI.ResponseAction>> completeFunction);

    AnvilGUI.Builder getAnvil();

    ItemBuilder buildAnvil(PipeInventory parent, Function<AnvilGUI.Completion, List<AnvilGUI.ResponseAction>> completeFunction);

    List<AnvilGUI.ResponseAction> closeThenReopenParent(PipeInventory parent);

    boolean validateInt(String text);

    boolean validateInt(String text, int min, int max);

}
