package fr.lacaleche.pipe.bukkit.modules.god.damage;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.modules.interfaces.ModuleFeature;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.god.damage.commands.DamageCommand;
import fr.lacaleche.pipe.bukkit.modules.god.damage.listeners.DamageListener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AModule(target = ModuleTarget.BUKKIT)
public class DamageModule extends Module {

    private final List<EntityDamageEvent.DamageCause> blackListDamageCauses;

    public DamageModule(IModuleHandler handler) {
        super(handler);
        this.blackListDamageCauses = new ArrayList<>();

        Arrays.stream(Features.values()).forEach(this::registerNewFeature);
        Arrays.stream(EntityDamageEvent.DamageCause.values()).forEach(this::blackListDamageCause);
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerBukkitListener(this, new DamageListener(this));
    }

    @Override
    public void registerCommands() {
        Pipe.get().getCommandManager().registerNewCommand(this, DamageCommand.class);
    }

    public void blackListDamageCause(EntityDamageEvent.DamageCause cause) {
        this.blackListDamageCauses.add(cause);
    }

    public void unblackListDamageCause(EntityDamageEvent.DamageCause cause) {
        this.blackListDamageCauses.remove(cause);
    }

    public boolean isDamageCauseBlackListed(EntityDamageEvent.DamageCause cause) {
        return this.blackListDamageCauses.contains(cause);
    }

    public List<EntityDamageEvent.DamageCause> getBlackListDamageCauses() {
        return blackListDamageCauses;
    }

    public enum Features implements ModuleFeature {
        PLAYER_DAMAGE_PLAYER,
        ENTITY_DAMAGE_PLAYER,
        PLAYER_DAMAGE_ENTITY,
        ENTITY_DAMAGE_ENTITY,
        ENTITY_DAMAGE,
        BLOCK_DAMAGE;

        @Override
        public String toString() {
            return super.name();
        }
    }

}
