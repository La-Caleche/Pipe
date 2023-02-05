package fr.lacaleche.pipe.bukkit.modules.nms.entities.shulker;

import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.DefaultStorage;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;

public class ShulkerStorage extends DefaultStorage {


    public ShulkerStorage(NMSManager nmsManager) {
        super(nmsManager);

        this.registerStorages();

    }

    private void registerStorages() {
        this.registerClass(StorageClass.SHULKER, this.getNmsManager().getNmsFinder().worldClass("entity.monster.EntityShulker"));
    }

}
