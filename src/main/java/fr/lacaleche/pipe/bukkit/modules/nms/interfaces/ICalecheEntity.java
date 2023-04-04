package fr.lacaleche.pipe.bukkit.modules.nms.interfaces;

import fr.lacaleche.core.utils.maths.Vector3;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketListenerPlayOut;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public interface ICalecheEntity {

    Packet<PacketListenerPlayOut> getSpawnPacket();

    PacketPlayOutEntityDestroy getDestroyPacket();

    PacketPlayOutEntityMetadata getMetadataPacket();

    void show(Player player);

    void hide(Player player);

    void updateMetadata();

    void updateMetadata(Player player);

    void commitPacket(Object packet);

    Set<Player> getViewers();

    int getId();

    Location getLocation();

    <T extends Entity> T getEntity();

    void setEntity(Entity entity);

    void spawn();

    void remove();

    void setLocation(Location location);

    void setInvisible(boolean invisible);

    void setGlowing(boolean glowing);

    void setNoGravity(boolean noGravity);

    boolean isNoGravity();

    void ride(ICalecheEntity entity);

    void ride(Player player);

    void addAsPassenger(ICalecheEntity entity);

    void addAsPassenger(Player player);

    void stopRiding();

    void removePassengers();

    void addSpectator(Player player);

    void removeSpectator(Player player);

    void tick();

}
