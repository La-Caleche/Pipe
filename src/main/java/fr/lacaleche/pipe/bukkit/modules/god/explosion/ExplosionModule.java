package fr.lacaleche.pipe.bukkit.modules.god.explosion;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.modules.interfaces.ModuleFeature;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.god.explosion.listeners.ExplosionListener;

import java.util.Arrays;

@AModule(target = ModuleTarget.BUKKIT)
public class ExplosionModule extends Module {

    public ExplosionModule(IModuleHandler handler) {
        super(handler);

        Arrays.stream(ExplosionModule.Features.values()).forEach(this::registerNewFeature);
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerBukkitListener(this, new ExplosionListener(this));
    }


    public enum Features implements ModuleFeature {
        BLOCK_EXPLODE,
        ENTITY_EXPLODE;

        @Override
        public String toString() {
            return super.name();
        }
    }

}
