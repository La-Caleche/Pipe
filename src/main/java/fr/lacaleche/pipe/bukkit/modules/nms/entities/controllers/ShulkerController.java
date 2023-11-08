package fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers;

import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheGolem;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheMonster;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;
import fr.lacaleche.pipe.bukkit.modules.nms.impls.MobController;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EntitySize;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMonsterType;
import net.minecraft.world.level.World;
import org.bukkit.Location;

public class ShulkerController extends MobController {

    public ShulkerController(NMSManager nmsManager, Location location) {
        super(nmsManager, location);
    }

    @Override
    public void spawn() {
        IStorage st = this.getStorage();

        this.setEntity(new CalecheShulker(st.handle(st.cast(StorageClass.CRAFT_WORLD, location.getWorld()))));
    }

    protected CalecheShulker shulker() {
        return this.getEntity();
    }

    public static class CalecheShulker extends CalecheGolem {

        public CalecheShulker(World world) {
            super(EntityTypes.aG, world);
        }

        @Override
        public EnumMonsterType eN() {
            return EnumMonsterType.b;
        }

        @Override
        protected float b(EntityPose entitypose, EntitySize entitysize) {
            return this.h_() ? 0.93F : 1.74F;
        }

        @Override
        public double bw() {
            return this.h_() ? 0.0 : -0.45;
        }
    }

}
