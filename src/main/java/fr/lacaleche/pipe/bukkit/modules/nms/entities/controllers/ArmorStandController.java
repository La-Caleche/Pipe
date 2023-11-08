package fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers;

import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheAmbient;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheLiving;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods;
import fr.lacaleche.pipe.bukkit.modules.nms.impls.LivingController;
import fr.lacaleche.pipe.bukkit.modules.nms.impls.MobController;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EntitySize;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class ArmorStandController extends LivingController {

    public ArmorStandController(NMSManager nmsManager, Location location) {
        super(nmsManager, location);
    }

    @Override
    public void spawn() {
        IStorage st = this.getStorage();

        this.setEntity(new ArmorStandController.CalecheArmorStand(st.handle(st.cast(StorageClass.CRAFT_WORLD, location.getWorld()))));
        this.setNoGravity(true);
    }

    public void setTitle(Component title) {
        net.minecraft.network.chat.IChatBaseComponent vanillaComponent = this.getStorage().construct(StorageConstructor.ADVENTURE_COMPONENT_CONSTRUCTOR, title);
        this.getStorage().invoke(StorageMethods.SET_CUSTOM_NAME, this.getEntity(), vanillaComponent);
        this.getStorage().invoke(StorageMethods.SET_CUSTOM_NAME_VISIBLE, this.getEntity(), true);

        this.enqueueUpdateMetadata();
    }

    public void setMarker(boolean marker) {
        this.armorStand().setMarker(marker);

        this.enqueueUpdateMetadata();
    }

    protected ArmorStandController.CalecheArmorStand armorStand() {
        return this.getEntity();
    }

    public static class CalecheArmorStand extends CalecheLiving {

        protected static final DataWatcherObject<Byte> DATA_CLIENT_FLAGS = DataWatcher.a(CalecheArmorStand.class, DataWatcherRegistry.a);

        public CalecheArmorStand(World world) {
            super(EntityTypes.d, world);
        }

        @Override
        protected void a_() {
            super.a_();
            this.am.a(DATA_CLIENT_FLAGS, (byte) 0);
        }

        @Override
        protected float b(EntityPose entitypose, EntitySize entitysize) {
            return entitysize.b * (this.h_() ? 0.5F : 0.9F);
        }

        @Override
        public double bw() {
            return 0;
        }

        public void setMarker(boolean marker) {
            this.am.b(DATA_CLIENT_FLAGS, this.setBit((Byte) this.am.b(DATA_CLIENT_FLAGS), 16, marker));
        }

        public boolean isMarker() {
            return ((Byte) this.am.b(DATA_CLIENT_FLAGS) & 16) != 0;
        }

        private byte setBit(byte value, int bitField, boolean set) {
            if (set) {
                value = (byte) (value | bitField);
            } else {
                value = (byte) (value & ~bitField);
            }

            return value;
        }

    }

}
