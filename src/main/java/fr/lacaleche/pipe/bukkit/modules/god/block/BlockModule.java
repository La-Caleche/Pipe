package fr.lacaleche.pipe.bukkit.modules.god.block;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.modules.interfaces.ModuleFeature;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.god.block.listeners.BlockListener;
import fr.lacaleche.pipe.bukkit.modules.god.entity.EntityModule;
import fr.lacaleche.pipe.bukkit.modules.god.entity.listeners.EntityListener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;

@AModule(target = ModuleTarget.BUKKIT)
public class BlockModule extends Module {

    public BlockModule(IModuleHandler handler) {
        super(handler);

        Arrays.stream(BlockModule.Features.values()).forEach(this::registerNewFeature);
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerBukkitListener(this, new BlockListener(this));
    }


    public enum Features implements ModuleFeature {
        FLUID_LEVEL_CHANGE,
        BLOCK_GROW,
        LEAVES_DECAY,
        BLOCK_PHYSICS,
        BLOCK_DROP_ITEM,
        BLOCK_FERTILIZE,
        PLAYER_HARVEST_BLOCK;

        @Override
        public String toString() {
            return super.name();
        }
    }

}
