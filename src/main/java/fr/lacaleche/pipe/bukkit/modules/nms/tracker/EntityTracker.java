package fr.lacaleche.pipe.bukkit.modules.nms.tracker;

import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.ICalecheEntity;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.EntityTrackerEntry;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Iterator;

public class EntityTracker {

    private final EntityTrackerEntry entityTracker;
    private final Entity entity;
    private final int range;
    private final ICalecheEntity calecheEntity;

    public EntityTracker(ICalecheEntity calecheEntity) {
        this.calecheEntity = calecheEntity;
        this.entity = calecheEntity.getEntity();
        this.entityTracker = new EntityTrackerEntry((WorldServer) entity.cG(), entity, 3, true, this::broadcast, Collections.emptySet());
        this.range = 8 * 16;
    }

    public boolean equals(Object object) {
        return object instanceof EntityTracker && ((EntityTracker) object).entity.ae() == this.entity.ae();
    }

    public int hashCode() {
        return this.entity.af();
    }

    public void broadcast(Packet<?> packet) {
        Iterator<Player> iterator = this.calecheEntity.getViewers().iterator();

        while (iterator.hasNext()) {
            Player player = iterator.next();
            ServerPlayerConnection serverplayerconnection = ((CraftPlayer) player).getHandle().b;

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