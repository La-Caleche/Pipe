package fr.lacaleche.pipe.bukkit.modules.nms.tracker;

import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.ICalecheEntity;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.EntityTrackerEntry;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Iterator;

public class EntityTracker {

    private final EntityTrackerEntry entityTracker;
    private final Entity entity;
    private final ICalecheEntity calecheEntity;

    public EntityTracker(ICalecheEntity calecheEntity) {
        this.calecheEntity = calecheEntity;
        this.entity = calecheEntity.getEntity();
        this.entityTracker = new EntityTrackerEntry((WorldServer) entity.dI(), entity, 3, true, this::broadcast, Collections.emptySet());
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof EntityTracker && ((EntityTracker) object).entity.ae() == this.entity.ae();
    }

    @Override
    public int hashCode() {
        return this.entity.af();
    }

    public void broadcast(Packet<?> packet) {
        for (Player player : this.calecheEntity.getViewers()) {
            ServerPlayerConnection serverplayerconnection = ((CraftPlayer) player).getHandle().c;

            serverplayerconnection.a(packet);
        }
    }

    public void broadcastAndSend(Packet<?> packet) {
        this.broadcast(packet);
    }

    public EntityTrackerEntry getTracker() {
        return entityTracker;
    }
}