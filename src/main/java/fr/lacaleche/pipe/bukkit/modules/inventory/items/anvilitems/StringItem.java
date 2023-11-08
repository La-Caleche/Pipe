package fr.lacaleche.pipe.bukkit.modules.inventory.items.anvilitems;

import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PipeInventory;
import fr.lacaleche.pipe.bukkit.modules.inventory.items.AbstractAnvilItem;
import fr.lacaleche.pipe.bukkit.modules.inventory.items.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class StringItem extends AbstractAnvilItem {

    public StringItem(ItemBuilder itemBuilder) {
        super(itemBuilder);
    }

    @Override
    public ItemBuilder buildAnvil(PipeInventory parent, BiFunction<Integer, AnvilGUI.StateSnapshot, List<AnvilGUI.ResponseAction>> clickHandler) {
        this.buildAnvil(parent, "Enter a text", Component.text("Prompt", NamedTextColor.GOLD), this.get(), clickHandler);
        return this.get();
    }

}
