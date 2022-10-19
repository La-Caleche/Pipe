package fr.lacaleche.pipe.bukkit.modules.god.health.listeners;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.bukkit.modules.god.health.HealthModule;
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
                if (this.module.isFeatureDisabled(HealthModule.Features.PLAYER_EAT_FOOD)) {
                    PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ PLAYER_EAT_FOOD is disabled".formatted(event.getClass().getSimpleName())));
                }
            } else {
                if (event.getFoodLevel() < player.getFoodLevel()) {
                    if (this.module.isFeatureDisabled(HealthModule.Features.PLAYER_LOOSE_FOOD)) {
                        PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ PLAYER_LOOSE_FOOD is disabled".formatted(event.getClass().getSimpleName())));
                    }
                } else {
                    if (this.module.isFeatureDisabled(HealthModule.Features.PLAYER_GAIN_FOOD)) {
                        PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ PLAYER_GAIN_FOOD is disabled".formatted(event.getClass().getSimpleName())));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent event) {
        PipeDebug.eventCalled(event);

        if (event.getEntity() instanceof Player) {
            if (this.module.isFeatureEnabled(HealthModule.Features.PLAYER_GAIN_HEALTH)) {
                if (this.module.isReasonsBlocked(event.getRegainReason())) {
                    PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ Regain reason was : %s".formatted(event.getClass().getSimpleName(), event.getRegainReason().name())));
                }
            } else {
                PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ PLAYER_GAIN_HEALTH is disabled".formatted(event.getClass().getSimpleName())));
            }
        } else {
            if (this.module.isFeatureEnabled(HealthModule.Features.ENTITY_GAIN_HEALTH)) {
                if (this.module.isReasonsBlocked(event.getRegainReason())) {
                    PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ Regain reason was : %s".formatted(event.getClass().getSimpleName(), event.getRegainReason().name())));
                }
            } else {
                PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ ENTITY_GAIN_HEALTH is disabled".formatted(event.getClass().getSimpleName())));
            }
        }
    }

}
