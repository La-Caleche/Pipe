package fr.lacaleche.pipe.bukkit.modules.nms.impls;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageFields.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods.*;
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
        this.setSpawnConstructor(PACKET_PLAY_OUT_SPAWN_ENTITY_CONSTRUCTOR);

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
        this.id = this.getStorage().invoke(GET_ID, this.getEntity());
        this.id = this.getEntity().hashCode();

        this.setLocation(this.getLocation());

        this.constructPackets();

        super.setEntity(entity);
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;

        this.getStorage().invoke(SET_LOCATION, this.getEntity(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.commitPacket(this.getStorage().construct(PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR, this.getEntity()));
    }

    @Override
    public void teleport(Location location) {
        Object packet = this.getStorage().construct(PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR, this.getEntity());

        this.getStorage().set(TP_PACKET$X, packet, location.getX());
        this.getStorage().set(TP_PACKET$Y, packet, location.getY());
        this.getStorage().set(TP_PACKET$Z, packet, location.getZ());
        this.getStorage().set(TP_PACKET$YAW, packet, (byte) (location.getYaw()/360*256));
        this.getStorage().set(TP_PACKET$PITCH, packet, (byte) (location.getPitch()/360*256));

        this.commitPacket(packet);

    }

    @Override
    public void setInvisible(boolean invisible) {
        this.getStorage().invoke(SET_INVISIBLE, this.getEntity(), invisible);

        this.updateMetadata();
    }

    @Override
    public void setGlowing(boolean glowing) {
        this.getStorage().invoke(SET_GLOWING, this.getEntity(), glowing);

        this.updateMetadata();
    }

    @Override
    public void setNoGravity(boolean noGravity) {
        this.getStorage().invoke(SET_NO_GRAVITY, this.getEntity(), noGravity);

        this.updateMetadata();
    }

    @Override
    public boolean isNoGravity() {
        return this.getStorage().invoke(IS_NO_GRAVITY, this.getEntity());
    }

    @Override
    public void ride(Player player) {
        this.getStorage().invoke(START_RIDING, this.getEntity(), this.getNmsManager().getPlayerHandle(player), true);

        this.updateMetadata();
    }

    @Override
    public void ride(ICalecheEntity entity) {
        this.getStorage().invoke(START_RIDING, this.getEntity(), entity.getEntity(), true);

        this.updateMetadata();
    }

    @Override
    public void addAsPassenger(Player player) {
        this.getStorage().invoke(START_RIDING, this.getNmsManager().getPlayerHandle(player), this.getEntity(), true);

        this.updateMetadata();
    }

    @Override
    public void addAsPassenger(ICalecheEntity entity) {
        this.getStorage().invoke(START_RIDING, entity.getEntity(), this.entity, true);

        this.updateMetadata();
    }

    @Override
    public void stopRiding() {
        this.getStorage().invoke(STOP_RIDING, this.getEntity());

        this.updateMetadata();
    }

    @Override
    public void removePassengers() {
        List<Object> passengers = this.getStorage().invoke(GET_PASSENGERS, this.getEntity());

        passengers.forEach(passenger -> this.getStorage().invoke(STOP_RIDING, passenger));

        this.updateMetadata();
    }

    @Override
    public void addSpectator(Player player) {
        this.getStorage().invoke(SET_CAMERA, this.getNmsManager().getPlayerHandle(player), this.getEntity());

        this.updateMetadata();
    }

    @Override
    public void removeSpectator(Player player) {
        this.getStorage().invoke(SET_CAMERA, this.getNmsManager().getPlayerHandle(player), (Object) null);

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
