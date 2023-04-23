package fr.lacaleche.pipe.bukkit.tabs.nms;

import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;

public class TabNMSManager extends NMSManager {

    public TabNMSManager() {
        super();
        this.setStorage(new TabStorage(this));
    }

}
