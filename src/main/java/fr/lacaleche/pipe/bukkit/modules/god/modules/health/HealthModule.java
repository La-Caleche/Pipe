package fr.lacaleche.pipe.bukkit.modules.god.modules.health;

import fr.lacaleche.core.modules.features.impl.Feature;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.god.annotations.AGodModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.health.commands.HealCommand;
import fr.lacaleche.pipe.bukkit.modules.god.modules.health.listeners.HealthListener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AGodModule
@AModule(target = ModuleTarget.BUKKIT)
public class HealthModule extends BukkitModule {

    private List<EntityRegainHealthEvent.RegainReason> blackListRegainReasons;

    public HealthModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        this.blackListRegainReasons = new ArrayList<>();
        Arrays.stream(EntityRegainHealthEvent.RegainReason.values()).forEach(this::blackListRegainReason);
    }

    @Override
    public void onDisable() {
        this.blackListRegainReasons.clear();
        this.blackListRegainReasons = null;
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

    public void blackListRegainReason(EntityRegainHealthEvent.RegainReason reason) {
        this.blackListRegainReasons.add(reason);
    }

    public void unblackListRegainReason(EntityRegainHealthEvent.RegainReason reason) {
        this.blackListRegainReasons.remove(reason);
    }

    public boolean isRegainReasonBlackListed(EntityRegainHealthEvent.RegainReason reason) {
        return this.blackListRegainReasons.contains(reason);
    }

    public List<EntityRegainHealthEvent.RegainReason> getBlackListRegainReason() {
        return blackListRegainReasons;
    }

    @Override
    public void registerFeatures() {
        this.getFeatureManager().registerFeature(new Feature<>("PLAYER_EAT_FOOD", false, Boolean.class));
        this.getFeatureManager().registerFeature(new Feature<>("PLAYER_LOOSE_FOOD", false, Boolean.class));
        this.getFeatureManager().registerFeature(new Feature<>("PLAYER_GAIN_FOOD", false, Boolean.class));
        this.getFeatureManager().registerFeature(new Feature<>("PLAYER_GAIN_HEALTH", false, Boolean.class));
        this.getFeatureManager().registerFeature(new Feature<>("ENTITY_GAIN_HEALTH", false, Boolean.class));
    }

}
