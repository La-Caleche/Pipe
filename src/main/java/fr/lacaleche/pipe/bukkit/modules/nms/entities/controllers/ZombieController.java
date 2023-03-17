package fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers;

import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheMonster;
import fr.lacaleche.pipe.bukkit.modules.nms.impls.LivingController;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ZombieController extends LivingController {

    public ZombieController(NMSManager nmsManager, Location location) {
        super(nmsManager, location);
    }

    @Override
    public void spawn() {
        IStorage st = this.getStorage();

        this.setEntity(new CalecheZombie(st.handle(st.cast(StorageClass.CRAFT_WORLD, location.getWorld()))));
    }

    protected CalecheZombie zombie() {
        return this.getEntity();
    }

    public static class CalecheZombie extends CalecheMonster {

        public CalecheZombie(World world) {
            super(EntityTypes.be, world);
        }

        @Override
        public EnumMonsterType eJ() {
            return EnumMonsterType.b;
        }

        @Override
        protected float b(EntityPose entitypose, EntitySize entitysize) {
            return this.y_() ? 0.93F : 1.74F;
        }

        @Override
        public double bu() {
            return this.y_() ? 0.0 : -0.45;
        }
    }

}
