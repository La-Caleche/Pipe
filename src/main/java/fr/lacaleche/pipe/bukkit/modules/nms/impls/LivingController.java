package fr.lacaleche.pipe.bukkit.modules.nms.impls;

import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.ICalecheLivingEntity;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import net.minecraft.world.entity.EntityLiving;
import org.bukkit.Location;

public abstract class LivingController extends AbstractController implements ICalecheLivingEntity {

    public LivingController(NMSManager nmsManager, Location location) {
        super(nmsManager, location);
        this.setSpawnConstructor(StorageConstructor.PACKET_PLAY_OUT_SPAWN_ENTITY_CONSTRUCTOR);
    }

    @Override
    public void tick() {
        this.getStorage().invoke(StorageMethods.TICK, this.getEntity());

        super.tick();
    }

}
