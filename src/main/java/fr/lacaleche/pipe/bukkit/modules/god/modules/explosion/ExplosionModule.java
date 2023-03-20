package fr.lacaleche.pipe.bukkit.modules.god.modules.explosion;

import fr.lacaleche.core.modules.features.impl.Feature;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.god.annotations.AGodModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.explosion.listeners.ExplosionListener;

@AGodModule
@AModule(target = ModuleTarget.BUKKIT)
public class ExplosionModule extends BukkitModule {

    public ExplosionModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerBukkitListener(this, new ExplosionListener(this));
    }

    @Override
    public void registerFeatures() {
        this.getFeatureManager().registerFeature(new Feature<>("BLOCK_EXPLODE", false, Boolean.class));
        this.getFeatureManager().registerFeature(new Feature<>("ENTITY_EXPLODE", false, Boolean.class));
    }

}
