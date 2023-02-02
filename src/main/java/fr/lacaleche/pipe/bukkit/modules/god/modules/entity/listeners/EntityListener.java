package fr.lacaleche.pipe.bukkit.modules.god.modules.entity.listeners;

import fr.lacaleche.core.modules.features.interfaces.IFeature;
import fr.lacaleche.pipe.bukkit.modules.god.modules.entity.EntityModule;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EntityListener implements Listener {

    private EntityModule module;

    public EntityListener(EntityModule module) {
        this.module = module;
    }

    private void cancelByFeature(Cancellable event, String featureName, boolean condition) {
        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, featureName, (f, value) -> value == condition);
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        PipeDebug.eventCalled(event);
        IFeature<Boolean> feature = this.module.getFeatureManager().getFeatureByName("ENTITY_DAMAGE");

        this.module.getFeatureManager().cancelConditionnaly(event, feature, (f, value) -> {
            if (value) {
                if (this.module.isDamageCauseBlackListed(event.getCause())) {
                    this.module.getFeatureManager().cancelEvent(event, feature, "Damage cause %s is blacklisted".formatted(event.getCause().name()));
                    return false;
                }

                return false;
            }

            return true;
        });
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        PipeDebug.eventCalled(event);
        String feature = null;

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            feature = "PLAYER_DAMAGE_PLAYER";
        } else {
            if (event.getEntity() instanceof Player) {
                feature = "ENTITY_DAMAGE_PLAYER";
            } else if (event.getDamager() instanceof Player) {
                feature = "PLAYER_DAMAGE_ENTITY";
            } else {
                feature = "ENTITY_DAMAGE_ENTITY";
            }
        }

        if (feature != null) {
            this.cancelByFeature(event, feature, false);
        }
    }

    @EventHandler
    public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent event) {
        PipeDebug.eventCalled(event);
        this.cancelByFeature(event, "BLOCK_DAMAGE", false);
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
