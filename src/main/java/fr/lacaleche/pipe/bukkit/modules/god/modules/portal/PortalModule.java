package fr.lacaleche.pipe.bukkit.modules.god.modules.portal;

import fr.lacaleche.core.modules.features.impl.Feature;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.god.annotations.AGodModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.portal.listeners.PortalListener;

@AGodModule
@AModule(target = ModuleTarget.BUKKIT)
public class PortalModule extends BukkitModule {

    public PortalModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.getBukkit().getListenerManager();
        bukkitManager.registerBukkitListener(this, new PortalListener(this));
    }

    @Override
    public void registerFeatures() {
        this.getFeatureManager().registerFeature(new Feature<>("ENTITY_PORTAL", false, Boolean.class));
        this.getFeatureManager().registerFeature(new Feature<>("PLAYER_PORTAL", false, Boolean.class));
    }

}
