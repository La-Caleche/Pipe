package fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheLiving;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods;
import fr.lacaleche.pipe.bukkit.modules.nms.impls.LivingController;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EntitySize;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.World;
import org.bukkit.Location;

public class HologramController extends LivingController {

    public HologramController(NMSManager nmsManager, Location location) {
        super(nmsManager, location);
    }

    @Override
    public void spawn() {
        IStorage st = this.getStorage();

        this.setEntity(new HologramController.CalecheHologram(st.handle(st.cast(StorageClass.CRAFT_WORLD, location.getWorld()))));
        this.setInvisible(true);
    }

    public void setTitle(Component title) {
        net.minecraft.network.chat.IChatBaseComponent vanillaComponent = this.getStorage().construct(StorageConstructor.ADVENTURE_COMPONENT_CONSTRUCTOR, title);
        this.getStorage().invoke(StorageMethods.SET_CUSTOM_NAME, this.getEntity(), vanillaComponent);
        this.getStorage().invoke(StorageMethods.SET_CUSTOM_NAME_VISIBLE, this.getEntity(), true);
    }

    protected HologramController.CalecheHologram hologram() {
        return this.getEntity();
    }

    public static class CalecheHologram extends CalecheLiving {

        public CalecheHologram(World world) {
            super(EntityTypes.c, world);
        }

        @Override
        protected float b(EntityPose entitypose, EntitySize entitysize) {
            return entitysize.b * (this.y_() ? 0.5F : 0.9F);
        }

        @Override
        public double bp() {
            return this.y_() ? 0.0 : 0.10000000149011612;
        }
    }

}
