package fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers;

import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheAmbient;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheLiving;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;
import fr.lacaleche.pipe.bukkit.modules.nms.impls.LivingController;
import fr.lacaleche.pipe.bukkit.modules.nms.impls.MobController;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EntitySize;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ambient.EntityBat;
import net.minecraft.world.level.World;
import org.bukkit.Location;

public class BatController extends MobController {

    public BatController(NMSManager nmsManager, Location location) {
        super(nmsManager, location);
    }

    @Override
    public void spawn() {
        IStorage st = this.getStorage();

        this.setEntity(new BatController.CalecheBat(st.handle(st.cast(StorageClass.CRAFT_WORLD, location.getWorld()))));
        this.setNoAi(true);
    }

    protected BatController.CalecheBat bat() {
        return this.getEntity();
    }

    public static class CalecheBat extends CalecheAmbient {

        public CalecheBat(World world) {
            super(EntityTypes.g, world);
        }

        @Override
        protected float b(EntityPose entitypose, EntitySize entitysize) {
            return entitysize.b / 2.0F;
        }

    }

}