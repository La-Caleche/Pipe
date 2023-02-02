package fr.lacaleche.pipe.bukkit.modules.god.modules.health.listeners;

import fr.lacaleche.pipe.bukkit.modules.god.modules.health.HealthModule;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HealthListener implements Listener {

    private HealthModule module;

    public HealthListener(HealthModule module) {
        this.module = module;
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        PipeDebug.eventCalled(event);
        if (event.getEntity() instanceof Player player) {
            if (event.getItem() != null) {
                this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "PLAYER_EAT_FOOD", (f, value) -> value);
            } else {
                this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, event.getFoodLevel() < player.getFoodLevel() ? "PLAYER_LOOSE_FOOD" : "PLAYER_GAIN_FOOD", (f, value) -> value);
            }
        }
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, event.getEntity() instanceof Player ? "PLAYER_GAIN_HEALTH" : "ENTITY_GAIN_HEALTH", (f, value) -> {
            if (value) {
                if (this.module.isRegainReasonBlackListed(event.getRegainReason())) {
                    this.module.getFeatureManager().cancelEvent(event, f, "Regain reason %s is blacklisted".formatted(event.getRegainReason().name()));
                    return false;
                }

                return false;
            }

            return true;
        });
    }

}
