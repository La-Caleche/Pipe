package fr.lacaleche.pipe.bukkit.modules.nms.impls;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageFields.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods.*;

import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageFields;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.ICalecheEntity;
import fr.lacaleche.pipe.common.tasks.interfaces.Task;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.SectionPosition;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.level.ChunkCoordIntPair;
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
    private final Set<Player> tmpViewers = new HashSet<>();

    public AbstractController(NMSManager nmsManager, Location location) {
        super(nmsManager);
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
        this.setLocation(this.getLocation());
        this.constructPackets();
        super.setEntity(entity);
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
        this.teleport(location);
        this.setPosition(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.getStorage().set(ENTITY$POSITION, this.getEntity(), this.getStorage().construct(VEC_3D_XYZ_CONSTRUCTOR, x, y, z));

        BlockPosition blockPosition = this.getStorage().get(ENTITY$BLOCK_POSITION, this.getEntity());
        ChunkCoordIntPair chunkPosition = this.getStorage().get(ENTITY$CHUNK_POSITION, this.getEntity());

        int i = MathHelper.a(x);
        int j = MathHelper.a(y);
        int k = MathHelper.a(z);

        int blockPositionX = this.getStorage().invoke(BLOCK_POSITION$GET_X, blockPosition);
        int blockPositionY = this.getStorage().invoke(BLOCK_POSITION$GET_Y, blockPosition);
        int blockPositionZ = this.getStorage().invoke(BLOCK_POSITION$GET_Z, blockPosition);

        int sectionCoordX = SectionPosition.a(blockPositionX);
        int sectionCoordZ = SectionPosition.a(blockPositionZ);

        int chunkPositionX = this.getStorage().get(CHUNK_POSITION$X, chunkPosition);
        int chunkPositionZ = this.getStorage().get(CHUNK_POSITION$Z, chunkPosition);

        if (i != blockPositionX || j != blockPositionY || k != blockPositionZ) {
            blockPosition = this.getStorage().construct(BLOCK_POSITION_XYZ_CONSTRUCTOR, i, j, k);
            this.getStorage().set(ENTITY$BLOCK_POSITION, this.getEntity(), blockPosition);
            if (sectionCoordX != chunkPositionX || sectionCoordZ != chunkPositionZ) {
                this.getStorage().set(ENTITY$CHUNK_POSITION, this.getEntity(), this.getStorage().construct(CHUNK_COORD_INT_PAIR_BLOCK_POSITION_CONSTRUCTOR, blockPosition));
            }
        }
    }

    @Override
    public void teleport(Location location) {
        this.teleport(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public void teleport(double x, double y, double z) {
        this.teleport(x, y, z, 0, 0);
    }

    @Override
    public void teleport(double x, double y, double z, float yaw, float pitch) {
        this.location = this.location.clone().set(x, y, z);
        this.location.setYaw(yaw);
        this.location.setPitch(pitch);

        Object packet = this.getStorage().construct(PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR, this.getEntity());

        this.getStorage().set(TP_PACKET$X, packet, x);
        this.getStorage().set(TP_PACKET$Y, packet, y);
        this.getStorage().set(TP_PACKET$Z, packet, z);
        this.getStorage().set(TP_PACKET$YAW, packet, (byte) (yaw/360*256));
        this.getStorage().set(TP_PACKET$PITCH, packet, (byte) (pitch/360*256));

        this.commitPacket(packet);
    }

    @Override
    public void setInvisible(boolean invisible) {
        this.getStorage().invoke(SET_INVISIBLE, this.getEntity(), invisible);
        this.enqueueUpdateMetadata();
    }

    @Override
    public void setGlowing(boolean glowing) {
        this.getStorage().invoke(SET_GLOWING, this.getEntity(), glowing);

        this.enqueueUpdateMetadata();
    }

    @Override
    public void setNoGravity(boolean noGravity) {
        this.getStorage().invoke(SET_NO_GRAVITY, this.getEntity(), noGravity);

        this.enqueueUpdateMetadata();
    }

    @Override
    public void setShiftKeyDown(boolean shiftKeyDown) {
        this.getStorage().invoke(SET_SHIFT_KEY_DOWN, this.getEntity(), shiftKeyDown);

        this.enqueueUpdateMetadata();
    }

    @Override
    public boolean isShiftKeyDown() {
        return this.getStorage().invoke(IS_SHIFT_KEY_DOWN, this.getEntity());
    }

    @Override
    public boolean isNoGravity() {
        return this.getStorage().invoke(IS_NO_GRAVITY, this.getEntity());
    }

    @Override
    public boolean isInvisible() {
        return this.getStorage().invoke(IS_INVISIBLE, this.getEntity());
    }

    @Override
    public void ride(Player player) {
        this.getStorage().invoke(START_RIDING, this.getEntity(), this.getNmsManager().getPlayerHandle(player), true);

        this.enqueueUpdateMetadata();
    }

    @Override
    public void ride(ICalecheEntity entity) {
        this.getStorage().invoke(START_RIDING, this.getEntity(), entity.getEntity(), true);

        this.enqueueUpdateMetadata();
    }

    @Override
    public void addAsPassenger(Player player) {
        this.getStorage().invoke(START_RIDING, this.getNmsManager().getPlayerHandle(player), this.getEntity(), true);

        this.enqueueUpdateMetadata();
    }

    @Override
    public void addAsPassenger(ICalecheEntity entity) {
        this.getStorage().invoke(START_RIDING, entity.getEntity(), this.entity, true);

        this.enqueueUpdateMetadata();
    }

    @Override
    public void stopRiding() {
        this.getStorage().invoke(STOP_RIDING, this.getEntity());

        this.enqueueUpdateMetadata();
    }

    @Override
    public void removePassengers() {
        List<Object> passengers = this.getStorage().invoke(GET_PASSENGERS, this.getEntity());

        passengers.forEach(passenger -> this.getStorage().invoke(STOP_RIDING, passenger));

        this.enqueueUpdateMetadata();
    }

    @Override
    public void addSpectator(Player player) {
        this.getStorage().invoke(SET_CAMERA, this.getNmsManager().getPlayerHandle(player), this.getEntity());

        this.enqueueUpdateMetadata();
    }

    @Override
    public void removeSpectator(Player player) {
        this.getStorage().invoke(SET_CAMERA, this.getNmsManager().getPlayerHandle(player), (Object) null);

        this.enqueueUpdateMetadata();
    }

    @Override
    public void remove() {
        super.remove();
        try {
            this.getEntity().ah();
        } catch (AssertionError error) {
            if (error.getMessage().startsWith("Unknown entity"))
                Logger.customDebugWCheck("Folia raised an error while trying to retire bukkit entity scheduler. But that's fine.");
            else throw error;
        }
    }

    @Override
    public Set<Player> getViewers() {
        return this.tmpViewers.isEmpty() ? this.viewers : this.tmpViewers;
    }

    @Override
    public void setTmpViewers(Set<Player> players) {
        this.resetTmpViewers();
        this.tmpViewers.addAll(players);
    }

    @Override
    public void resetTmpViewers() {
        this.tmpViewers.clear();
    }
}
