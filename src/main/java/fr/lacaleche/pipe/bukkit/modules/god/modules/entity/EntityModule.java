package fr.lacaleche.pipe.bukkit.modules.god.modules.entity;

import fr.lacaleche.core.modules.features.impl.Feature;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.god.annotations.AGodModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.entity.commands.EntityCommand;
import fr.lacaleche.pipe.bukkit.modules.god.modules.entity.listeners.EntityListener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AGodModule
@AModule(target = ModuleTarget.BUKKIT)
public class EntityModule extends BukkitModule {

    private final List<EntityDamageEvent.DamageCause> blackListDamageCauses;

    public EntityModule(IModuleHandler handler) {
        super(handler);
        this.blackListDamageCauses = new ArrayList<>();

        this.registerFeatures();

        Arrays.stream(EntityDamageEvent.DamageCause.values()).forEach(this::blackListDamageCause);
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerBukkitListener(this, new EntityListener(this));
    }

    @Override
    public void registerCommands() {
        Pipe.get().getCommandManager().registerNewCommand(this, EntityCommand.class);
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

    private void registerFeatures() {
        this.getFeatureManager().registerFeature(new Feature<>("PLAYER_DAMAGE_PLAYER", false));
        this.getFeatureManager().registerFeature(new Feature<>("ENTITY_DAMAGE_PLAYER", false));
        this.getFeatureManager().registerFeature(new Feature<>("PLAYER_DAMAGE_ENTITY", false));
        this.getFeatureManager().registerFeature(new Feature<>("ENTITY_DAMAGE_ENTITY", false));
        this.getFeatureManager().registerFeature(new Feature<>("ENTITY_DAMAGE", false));
        this.getFeatureManager().registerFeature(new Feature<>("BLOCK_DAMAGE", false));
    }

}
