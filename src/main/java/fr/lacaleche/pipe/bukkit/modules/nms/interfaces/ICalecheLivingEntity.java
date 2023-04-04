package fr.lacaleche.pipe.bukkit.modules.nms.interfaces;

import org.bukkit.inventory.ItemStack;

public interface ICalecheLivingEntity {

    void setItemSlot(String slot, ItemStack itemStack);

    void setHelmet(ItemStack itemStack);

    void setChestplate(ItemStack itemStack);

    void setLeggings(ItemStack itemStack);

    void setBoots(ItemStack itemStack);

    void setMainHand(ItemStack itemStack);

    void setOffHand(ItemStack itemStack);

}
