package fr.lacaleche.pipe.bukkit.modules.nms.impls;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageFields;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.ICalecheEntity;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import fr.lacaleche.pipe.bukkit.modules.nms.tracker.EntityTracker;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods.*;

import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import fr.lacaleche.pipe.common.tasks.interfaces.Task;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseController implements ICalecheEntity {

    private final NMSManager nmsManager;
    private Packet<PacketListenerPlayOut> packetPlayOutSpawnEntity;
    private PacketPlayOutEntityDestroy packetPlayOutEntityDestroy;
    private EntityTracker entityTracker;

    private StorageConstructor spawnConstructor;
    private StorageConstructor destroyConstructor;

    private Task tick;
    private boolean noTick = false;
    private boolean needUpdateMetadata;

    public BaseController(NMSManager nmsManager) {
        this.nmsManager = nmsManager;
        this.needUpdateMetadata = false;

        this.spawnConstructor = PACKET_PLAY_OUT_SPAWN_ENTITY_CONSTRUCTOR;
        this.destroyConstructor = PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entityTracker = new EntityTracker(this);

        this.tick = Pipe.getBukkit().getTaskManager().newTask(taskBuilder -> taskBuilder.loop(true).run(this::taskLoop));
        this.tick();
    }

    @Override
    public void setNoTick(boolean noTick) {
        this.noTick = noTick;
    }

    @Override
    public boolean isNoTick() {
        return noTick;
    }

    @Override
    public void tick() {
        this.entityTracker.getTracker().a();
    }

    @Override
    public void taskLoop(Task task) {
        if (!this.noTick) this.tick();

        if (this.needUpdateMetadata) {
            this.updateMetadata();
            this.needUpdateMetadata = false;
        }
    }

    public void setSpawnConstructor(StorageConstructor spawnConstructor) {
        this.spawnConstructor = spawnConstructor;
    }

    public void setDestroyConstructor(StorageConstructor destroyConstructor) {
        this.destroyConstructor = destroyConstructor;
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
        DataWatcher dataWatcher = this.getStorage().invoke(GET_DATA_WATCHER, this.getEntity());
        Int2ObjectMap<DataWatcher.Item<?>> itemsById = this.getStorage().get(StorageFields.MDP_ITEMS_BY_ID, dataWatcher);
        return this.getStorage().construct(PACKET_PLAY_OUT_ENTITY_METADATA_CONSTRUCTOR, this.getId(), this.getTrackedValues(itemsById));
    }

    private List<?> getTrackedValues(Int2ObjectMap<DataWatcher.Item<?>> itemsById) {
        List<DataWatcher.b<?>> list = null;
        ObjectIterator objectiterator = itemsById.values().iterator();

        while(objectiterator.hasNext()) {
            DataWatcher.Item<?> datawatcher_item = (DataWatcher.Item)objectiterator.next();
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(datawatcher_item.e());
        }

        return list;
    }

    @Override
    public void show(Player player) {
        if (this.getViewers().add(player)) {
            this.getNmsManager().sendPacket(player, this.getSpawnPacket());
            this.updateMetadata(player);
        }
    }

    @Override
    public boolean canSee(Player player) {
        return this.getViewers().contains(player);
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
        for (Player player : this.getViewers()) {
            this.getNmsManager().sendPacket(player, packet);
        }
    }

    @Override
    public void remove() {
        this.tick.stop();
    }

    @Override
    public void enqueueUpdateMetadata() {
        this.needUpdateMetadata = true;
    }

    public void constructPackets() {
        this.packetPlayOutSpawnEntity = this.getStorage().construct(this.spawnConstructor, this.getEntity());
        this.packetPlayOutEntityDestroy = this.getStorage().construct(this.destroyConstructor, new int [] {this.getId()});
    }

    public IStorage getStorage() {
        return this.getNmsManager().getStorage();
    }

    public NMSManager getNmsManager() {
        return nmsManager;
    }

}
