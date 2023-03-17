package fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheLiving;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods;
import fr.lacaleche.pipe.bukkit.modules.nms.impls.LivingController;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EntitySize;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.bukkit.Location;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

        private static final DataWatcherObject<Byte> bB = DataWatcher.a(EntityArmorStand.class, DataWatcherRegistry.a);

        public CalecheHologram(World world) {
            super(EntityTypes.d, world);
        }

        @Override
        protected float b(EntityPose entitypose, EntitySize entitysize) {
            return entitysize.b * (this.y_() ? 0.5F : 0.9F);
        }

        @Override
        public double bu() {
            return this.y_() ? 0.0 : 0.10000000149011612;
        }

    }

}
