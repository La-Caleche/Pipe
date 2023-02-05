package fr.lacaleche.pipe.bukkit.modules.nms.entities.interfaces;

import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public interface ICalecheEntity {

    Set<Player> getViewers();

    int getId();

    Location getLocation();

    Entity getEntity();

    PacketPlayOutSpawnEntityLiving getSpawnPacket();

    PacketPlayOutEntityDestroy getDestroyPacket();

    PacketPlayOutEntityMetadata getMetadataPacket();

    void spawn();

    void show(Player player);

    void hide(Player player);

    void setLocation(Location location);

    void updateLocation();

    void updateMetadata();

    void updateMetadata(Player player);

    void commitPacket(Object packet);

    void setEntity(Entity entity);

    void remove();

}
