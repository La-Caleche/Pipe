package fr.lacaleche.pipe.bukkit.modules.nms.impls;

import com.mojang.datafixers.util.Pair;
import fr.lacaleche.core.utils.CalecheDebug;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers.ZombieController;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.ICalecheLivingEntity;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.monster.EntityZombie;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

import java.util.List;

public abstract class LivingController extends AbstractController implements ICalecheLivingEntity {

    public LivingController(NMSManager nmsManager, Location location) {
        super(nmsManager, location);
    }

    @Override
    public void tick() {
        this.getStorage().invoke(StorageMethods.TICK, this.getEntity());

        super.tick();
    }

    @Override
    public void setItemSlot(String slot, ItemStack itemStack) {
        Enum<?> enumSlot = Enum.valueOf((Class<Enum>) this.getStorage().clazz(StorageClass.EQUIPMENT_SLOT), slot);
        this.commitPacket(this.getStorage().construct(StorageConstructor.PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR, this.getId(), List.of(Pair.of(enumSlot, CraftItemStack.asNMSCopy(itemStack)))));
    }

    @Override
    public void setHelmet(ItemStack itemStack) {
        this.setItemSlot("HEAD", itemStack);
    }

    @Override
    public void setChestplate(ItemStack itemStack) {
        this.setItemSlot("CHEST", itemStack);
    }

    @Override
    public void setLeggings(ItemStack itemStack) {
        this.setItemSlot("LEGS", itemStack);
    }

    @Override
    public void setBoots(ItemStack itemStack) {
        this.setItemSlot("FEET", itemStack);
    }

    @Override
    public void setMainHand(ItemStack itemStack) {
        this.setItemSlot("MAINHAND", itemStack);
    }

    @Override
    public void setOffHand(ItemStack itemStack) {
        this.setItemSlot("OFFHAND", itemStack);
    }

}
