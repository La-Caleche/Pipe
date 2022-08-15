package fr.lacaleche.pipe.bukkit.modules.god.damage.listeners;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.bukkit.modules.god.damage.DamageModule;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private DamageModule module;

    public DamageListener(DamageModule module) {
        this.module = module;
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureEnabled(DamageModule.Features.ENTITY_DAMAGE)) {
            if (this.module.isDamageCauseBlackListed(event.getCause())) {
                PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ Damage cause was : %s".formatted(event.getClass().getSimpleName(), event.getCause().name())));
            }
        } else {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ ENTITY_DAMAGE is disabled".formatted(event.getClass().getSimpleName())));
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        PipeDebug.eventCalled(event);
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (this.module.isFeatureDisabled(DamageModule.Features.PLAYER_DAMAGE_PLAYER)) {
                PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ PLAYER_DAMAGE_PLAYER is disabled".formatted(event.getClass().getSimpleName())));
            }
        } else {
            if (event.getEntity() instanceof Player) {
                if (this.module.isFeatureDisabled(DamageModule.Features.ENTITY_DAMAGE_PLAYER)) {
                    PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ ENTITY_DAMAGE_PLAYER is disabled".formatted(event.getClass().getSimpleName())));
                }
            } else if (event.getDamager() instanceof Player) {
                if (this.module.isFeatureDisabled(DamageModule.Features.PLAYER_DAMAGE_ENTITY)) {
                    PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ PLAYER_DAMAGE_ENTITY is disabled".formatted(event.getClass().getSimpleName())));
                }
            } else {
                if (this.module.isFeatureDisabled(DamageModule.Features.ENTITY_DAMAGE_ENTITY)) {
                    PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ ENTITY_DAMAGE_ENTITY is disabled".formatted(event.getClass().getSimpleName())));
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureDisabled(DamageModule.Features.BLOCK_DAMAGE)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ BLOCK_DAMAGE is disabled".formatted(event.getClass().getSimpleName())));
        }
    }


    private DamageModule getModule() {
        return this.module;
    }

}
