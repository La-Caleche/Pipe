package fr.lacaleche.pipe.bukkit.modules.nms.impls;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.ICalecheLivingEntity;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.ICalecheMobEntity;
import org.bukkit.Location;

public abstract class MobController extends LivingController implements ICalecheMobEntity {

    public MobController(NMSManager nmsManager, Location location) {
        super(nmsManager, location);
    }

    @Override
    public void tick() {
        if (this.isNoAi()) return;
        super.tick();
    }

    @Override
    public void setNoAi(boolean noAi) {
        this.getStorage().invoke(StorageMethods.SET_NO_AI, this.getEntity(), noAi);

        this.updateMetadata();
    }

    @Override
    public boolean isNoAi() {
        return this.getStorage().invoke(StorageMethods.IS_NO_AI, this.getEntity());
    }
}
