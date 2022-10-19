package fr.lacaleche.pipe.bukkit.modules.god.portal;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.modules.interfaces.ModuleFeature;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.god.portal.listeners.PortalListener;

import java.util.Arrays;

@AModule(target = ModuleTarget.BUKKIT)
public class PortalModule extends Module {

    public PortalModule(IModuleHandler handler) {
        super(handler);

        Arrays.stream(PortalModule.Features.values()).forEach(this::registerNewFeature);
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerBukkitListener(this, new PortalListener(this));
    }


    public enum Features implements ModuleFeature {
        ENTITY_PORTAL,
        PLAYER_PORTAL;

        @Override
        public String toString() {
            return super.name();
        }
    }

}
