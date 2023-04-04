package fr.lacaleche.pipe.bukkit.modules.nms.impls;

import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.ICalecheEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EntityZombie;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractController extends BaseController {

    protected int id;
    protected Entity entity;
    protected Location location;

    private final Set<Player> viewers = new HashSet<>();

    public AbstractController(NMSManager nmsManager, Location location) {
        super(nmsManager);
        this.setSpawnConstructor(StorageConstructor.PACKET_PLAY_OUT_SPAWN_ENTITY_CONSTRUCTOR);

        this.location = location;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public <T extends Entity> T getEntity() {
        return (T) this.entity;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
        this.id = this.getStorage().invoke(StorageMethods.GET_ID, this.getEntity());
        this.id = this.getEntity().hashCode();

        this.setLocation(this.getLocation());

        this.constructPackets();

        super.setEntity(entity);
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;

        this.getStorage().invoke(StorageMethods.SET_LOCATION, this.getEntity(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.commitPacket(this.getStorage().construct(StorageConstructor.PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR, this.getEntity()));
    }

    @Override
    public void setInvisible(boolean invisible) {
        this.getStorage().invoke(StorageMethods.SET_INVISIBLE, this.getEntity(), invisible);

        this.updateMetadata();
    }

    @Override
    public void setGlowing(boolean glowing) {
        this.getStorage().invoke(StorageMethods.SET_GLOWING, this.getEntity(), glowing);

        this.updateMetadata();
    }

    @Override
    public void setNoGravity(boolean noGravity) {
        this.getStorage().invoke(StorageMethods.SET_NO_GRAVITY, this.getEntity(), noGravity);

        this.updateMetadata();
    }

    @Override
    public boolean isNoGravity() {
        return this.getStorage().invoke(StorageMethods.IS_NO_GRAVITY, this.getEntity());
    }

    @Override
    public void ride(Player player) {
        this.getStorage().invoke(StorageMethods.START_RIDING, this.getEntity(), this.getNmsManager().getPlayerHandle(player), true);

        this.updateMetadata();
    }

    @Override
    public void ride(ICalecheEntity entity) {
        this.getStorage().invoke(StorageMethods.START_RIDING, this.getEntity(), entity.getEntity(), true);

        this.updateMetadata();
    }

    @Override
    public void addAsPassenger(Player player) {
        this.getStorage().invoke(StorageMethods.START_RIDING, this.getNmsManager().getPlayerHandle(player), this.getEntity(), true);

        this.updateMetadata();
    }

    @Override
    public void addAsPassenger(ICalecheEntity entity) {
        this.getStorage().invoke(StorageMethods.START_RIDING, entity.getEntity(), this.entity, true);

        this.updateMetadata();
    }

    @Override
    public void stopRiding() {
        this.getStorage().invoke(StorageMethods.STOP_RIDING, this.getEntity());

        this.updateMetadata();
    }

    @Override
    public void removePassengers() {
        List<Object> passengers = this.getStorage().invoke(StorageMethods.GET_PASSENGERS, this.getEntity());

        passengers.forEach(passenger -> this.getStorage().invoke(StorageMethods.STOP_RIDING, passenger));

        this.updateMetadata();
    }

    @Override
    public void addSpectator(Player player) {
        this.getStorage().invoke(StorageMethods.SET_CAMERA, this.getNmsManager().getPlayerHandle(player), this.getEntity());

        this.updateMetadata();
    }

    @Override
    public void removeSpectator(Player player) {
        this.getStorage().invoke(StorageMethods.SET_CAMERA, this.getNmsManager().getPlayerHandle(player), (Object) null);

        this.updateMetadata();
    }

    @Override
    public void remove() {
        super.remove();
        this.getEntity().ah();
    }

    @Override
    public Set<Player> getViewers() {
        return this.viewers;
    }

}
