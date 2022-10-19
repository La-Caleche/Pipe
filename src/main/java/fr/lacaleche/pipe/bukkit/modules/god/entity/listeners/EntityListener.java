package fr.lacaleche.pipe.bukkit.modules.god.entity.listeners;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.bukkit.modules.god.entity.EntityModule;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EntityListener implements Listener {

    private EntityModule module;

    public EntityListener(EntityModule module) {
        this.module = module;
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureEnabled(EntityModule.Features.ENTITY_DAMAGE)) {
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
            if (this.module.isFeatureDisabled(EntityModule.Features.PLAYER_DAMAGE_PLAYER)) {
                PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ PLAYER_DAMAGE_PLAYER is disabled".formatted(event.getClass().getSimpleName())));
            }
        } else {
            if (event.getEntity() instanceof Player) {
                if (this.module.isFeatureDisabled(EntityModule.Features.ENTITY_DAMAGE_PLAYER)) {
                    PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ ENTITY_DAMAGE_PLAYER is disabled".formatted(event.getClass().getSimpleName())));
                }
            } else if (event.getDamager() instanceof Player) {
                if (this.module.isFeatureDisabled(EntityModule.Features.PLAYER_DAMAGE_ENTITY)) {
                    PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ PLAYER_DAMAGE_ENTITY is disabled".formatted(event.getClass().getSimpleName())));
                }
            } else {
                if (this.module.isFeatureDisabled(EntityModule.Features.ENTITY_DAMAGE_ENTITY)) {
                    PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ ENTITY_DAMAGE_ENTITY is disabled".formatted(event.getClass().getSimpleName())));
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureDisabled(EntityModule.Features.BLOCK_DAMAGE)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ BLOCK_DAMAGE is disabled".formatted(event.getClass().getSimpleName())));
        }
    }

    @EventHandler
    public void onEntitySpawnEvent(EntitySpawnEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onEntityTameEvent(EntityTameEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onEntityBreedEvent(EntityBreedEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onEntityToggleGlideEvent(EntityToggleGlideEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onEntityTargetEvent(EntityTargetEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onEntitySpellCastEvent(EntitySpellCastEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onEntityPlaceEvent(EntityPlaceEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onEntityRegainHealthEvent(EntityRegainHealthEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onEntityPotionEffectEvent(EntityPotionEffectEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onEntityDropItemEvent(EntityDropItemEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onSpawnerSpawnEvent(SpawnerSpawnEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        PipeDebug.eventCalled(event);
    }

}
