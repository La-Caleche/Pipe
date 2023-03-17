package fr.lacaleche.pipe.bukkit.modules.nms.impls;

import fr.lacaleche.core.utils.Logger;
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
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang.Validate;
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
        DataWatcher dataWatcher = this.getStorage().invoke(GET_DATA_WATCHER, this.getEntity());
        Field f = this.getStorage().field(StorageFields.MDP_ITEMS_BY_ID);
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
        this.packetPlayOutSpawnEntity = this.getStorage().construct(PACKET_PLAY_OUT_SPAWN_ENTITY_CONSTRUCTOR, this.getEntity());
        this.packetPlayOutEntityDestroy = this.getStorage().construct(PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR, new int [] {this.getId()});
    }

    public IStorage getStorage() {
        return this.getNmsManager().getStorage();
    }

    public NMSManager getNmsManager() {
        return nmsManager;
    }

}
