package fr.lacaleche.pipe.bukkit.modules.nms.impls;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.ICalecheEntity;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import fr.lacaleche.pipe.bukkit.modules.nms.tracker.EntityTracker;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods.*;

import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import fr.lacaleche.pipe.common.tasks.interfaces.Task;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.Player;

public abstract class BaseController implements ICalecheEntity {

    private final NMSManager nmsManager;
    private Packet<PacketListenerPlayOut> packetPlayOutSpawnEntity;
    private PacketPlayOutEntityDestroy packetPlayOutEntityDestroy;
    private EntityTracker entityTracker;

    private StorageConstructor spawnConstructor;

    private Task tick;

    public BaseController(NMSManager nmsManager) {
        this.nmsManager = nmsManager;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entityTracker = new EntityTracker(this);

        this.tick = Pipe.get().getTaskManager().newTask(new TaskBuilder().loop(true).callback((tick) -> {
            this.tick();
        }));
    }

    @Override
    public void tick() {
        this.entityTracker.getTracker().a();
    }

    public void setSpawnConstructor(StorageConstructor spawnConstructor) {
        this.spawnConstructor = spawnConstructor;
    }

    @Override
    public Packet<PacketListenerPlayOut> getSpawnPacket() {
        return packetPlayOutSpawnEntity;
    }

    @Override
    public PacketPlayOutEntityDestroy getDestroyPacket() {
        return packetPlayOutEntityDestroy;
    }

    @Override
    public PacketPlayOutEntityMetadata getMetadataPacket() {
        return this.getStorage().construct(PACKET_PLAY_OUT_ENTITY_METADATA_CONSTRUCTOR, this.getId(), this.getStorage().invoke(GET_DATA_WATCHER, this.getEntity()), true);
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
        this.tick.stop();
    }

    public void constructPackets() {
        this.packetPlayOutSpawnEntity = this.getStorage().construct(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CONSTRUCTOR, this.getEntity());
        this.packetPlayOutEntityDestroy = this.getStorage().construct(PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR, new int [] {this.getId()});
    }

    public IStorage getStorage() {
        return this.getNmsManager().getStorage();
    }

    public NMSManager getNmsManager() {
        return nmsManager;
    }

}
