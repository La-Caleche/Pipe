package fr.lacaleche.pipe.bukkit.modules.nms.entities;

import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.interfaces.ICalecheEntity;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.interfaces.IStorage;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public abstract class CalecheEntity implements ICalecheEntity {

    private final NMSManager nmsManager;
    private final IStorage storage;

    protected int id;
    protected Entity entity;
    protected Location location;

    private PacketPlayOutSpawnEntityLiving packetPlayOutSpawnEntityLiving;
    private PacketPlayOutEntityDestroy packetPlayOutEntityDestroy;

    private final Set<Player> viewers = new HashSet<>();

    public CalecheEntity(NMSManager nmsManager, IStorage storage, Location location) {
        this.nmsManager = nmsManager;
        this.storage = storage;
        this.location = location;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
        this.id = this.getStorage().invoke(StorageMethods.GET_ID, this.getEntity());
        this.id = this.getEntity().hashCode();

        this.getStorage().invoke(StorageMethods.SET_LOCATION, this.getEntity(), this.getLocation().getX(), this.getLocation().getY(), this.getLocation().getZ(), this.getLocation().getYaw(), this.getLocation().getPitch());

        this.packetPlayOutSpawnEntityLiving = this.getStorage().construct(StorageConstructor.PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CONSTRUCTOR, this.getEntity());
        this.packetPlayOutEntityDestroy = this.getStorage().construct(StorageConstructor.PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR, new int[] { this.getId() });
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public PacketPlayOutSpawnEntityLiving getSpawnPacket() {
        return packetPlayOutSpawnEntityLiving;
    }

    @Override
    public PacketPlayOutEntityDestroy getDestroyPacket() {
        return packetPlayOutEntityDestroy;
    }

    @Override
    public void show(Player player) {
        if (this.getViewers().add(player)) {
            this.getNmsManager().sendPacket(player, this.getSpawnPacket());
            this.updateMetadata(player);
        }
    }

    @Override
    public void hide(Player player) {
        if (this.getViewers().remove(player)) {
            this.getNmsManager().sendPacket(player, this.getDestroyPacket());
        }
    }

    @Override
    public void setLocation(Location location) {
        this.getStorage().invoke(StorageMethods.SET_LOCATION, this.getEntity(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.location = location;
        this.updateLocation();
    }

    @Override
    public void updateLocation() {
        this.commitPacket(this.getStorage().construct(StorageConstructor.PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR, this.getEntity()));
    }

    @Override
    public void updateMetadata() {
        this.commitPacket(this.getMetadataPacket());
    }

    @Override
    public void updateMetadata(Player player) {
        this.getNmsManager().sendPacket(player, this.getMetadataPacket());
    }

    @Override
    public void commitPacket(Object packet) {
        this.getViewers().forEach((Player player) -> this.getNmsManager().sendPacket(player, packet));
    }

    @Override
    public void remove() {
        this.getEntity().ah();
    }

    @Override
    public PacketPlayOutEntityMetadata getMetadataPacket() {
        return this.getStorage().construct(StorageConstructor.PACKET_PLAY_OUT_ENTITY_METADATA_CONSTRUCTOR, this.getId(), this.getStorage().invoke(StorageMethods.GET_DATA_WATCHER, this.getEntity()), true);
    }

    @Override
    public Set<Player> getViewers() {
        return this.viewers;
    }

    public IStorage getStorage() {
        return storage;
    }

    public NMSManager getNmsManager() {
        return nmsManager;
    }

}
