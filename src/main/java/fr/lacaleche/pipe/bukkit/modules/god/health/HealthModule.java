package fr.lacaleche.pipe.bukkit.modules.god.health;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.modules.interfaces.ModuleFeature;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.god.health.commands.HealCommand;
import fr.lacaleche.pipe.bukkit.modules.god.health.listeners.HealthListener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AModule(target = ModuleTarget.BUKKIT)
public class HealthModule extends Module {

    private final List<EntityRegainHealthEvent.RegainReason> reasons;

    public HealthModule(IModuleHandler handler) {
        super(handler);
        this.reasons = new ArrayList<>();

        Arrays.stream(Features.values()).forEach(this::registerNewFeature);
        Arrays.stream(EntityRegainHealthEvent.RegainReason.values()).forEach(this::addRegainReason);
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerBukkitListener(this, new HealthListener(this));
    }

    @Override
    public void registerCommands() {
        Pipe.get().getCommandManager().registerNewCommand(this, HealCommand.class);
    }

    public void addRegainReason(EntityRegainHealthEvent.RegainReason reason) {
        this.reasons.add(reason);
    }

    public void removeRegainReason(EntityRegainHealthEvent.RegainReason reason) {
        this.reasons.remove(reason);
    }

    public boolean isReasonsBlocked(EntityRegainHealthEvent.RegainReason reason) {
        return this.reasons.contains(reason);
    }

    public List<EntityRegainHealthEvent.RegainReason> getReasons() {
        return reasons;
    }

    public enum Features implements ModuleFeature {
        PLAYER_EAT_FOOD,
        PLAYER_LOOSE_FOOD,
        PLAYER_GAIN_FOOD,
        PLAYER_GAIN_HEALTH,
        ENTITY_GAIN_HEALTH;

        @Override
        public String toString() {
            return super.name();
        }
    }

}
