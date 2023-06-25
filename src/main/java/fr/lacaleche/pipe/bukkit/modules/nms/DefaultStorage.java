package fr.lacaleche.pipe.bukkit.modules.nms;

import com.mojang.authlib.GameProfile;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageFields.*;

import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageFields;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.enums.IStorageClass;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.enums.IStorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.enums.IStorageFields;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.enums.IStorageMethods;
import fr.lacaleche.pipe.bukkit.modules.nms.utils.ClassFinder;
import net.kyori.adventure.text.Component;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.monster.EntityMonster;
import net.minecraft.world.inventory.Containers;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class DefaultStorage implements IStorage {

    private final NMSManager nmsManager;

    private final Map<IStorageClass, Class<?>> clazz;
    private final Map<IStorageConstructor, Constructor<?>> constructors;
    private final Map<IStorageMethods, Method> methods;
    private final Map<IStorageFields, Field> fields;

    public DefaultStorage(NMSManager nmsManager) {
        this.nmsManager = nmsManager;

        this.clazz = new HashMap<>();
        this.constructors = new HashMap<>();
        this.methods = new HashMap<>();
        this.fields = new HashMap<>();

        this.registerDefaults();
    }

    @Override
    public Class<?> clazz(IStorageClass storageClass) {
        return this.clazz.getOrDefault(storageClass, null);
    }

    @Override
    public Constructor<?> constructor(IStorageConstructor storageConstructor) {
        return this.constructors.getOrDefault(storageConstructor, null);
    }

    @Override
    public Method method(IStorageMethods storageMethod) {
        return this.methods.getOrDefault(storageMethod, null);
    }

    @Override
    public Field field(IStorageFields storageField) {
        Field field = this.fields.getOrDefault(storageField, null);
        if (field != null) field.setAccessible(true);
        return field;
    }

    @Override
    public void registerClass(IStorageClass storageClass, Class<?> clazz) {
        this.clazz.putIfAbsent(storageClass, clazz);
    }

    @Override
    public void registerConstructor(IStorageConstructor storageConstructor, Constructor<?> constructor) {
        this.constructors.putIfAbsent(storageConstructor, constructor);
    }

    @Override
    public void registerMethod(IStorageMethods storageMethod, Method method) {
        this.methods.putIfAbsent(storageMethod, method);
    }

    @Override
    public void registerField(IStorageFields storageField, Field field) {
        this.fields.putIfAbsent(storageField, field);
    }

    @Override
    public NMSManager getNmsManager() {
        return nmsManager;
    }

    @Override
    public <T> T invoke(IStorageMethods storageMethod, Object instance, Object... args) {
        try {
            this.method(storageMethod).setAccessible(true);
            return (T) this.method(storageMethod).invoke(instance, args);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    @Override
    public <T> T construct(IStorageConstructor storageConstructor, Object... args) {
        try {
            return (T) this.constructor(storageConstructor).newInstance(args);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    @Override
    public <T> T cast(IStorageClass storageClass, Object instance) {
        try {
            return (T) this.clazz(storageClass).cast(instance);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    @Override
    public <T> Constructor<T> getConstructor(IStorageClass storageClass, Class<?>... args) {
        try {
            return (Constructor<T>) this.clazz(storageClass).getConstructor(args);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    @Override
    public <T> T handle(Object objet) {
        try {
            return (T) this.getNmsManager().getClassFinder().getHandle(objet);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    @Override
    public <T> Method getMethod(IStorageClass storageClass, String name, Class<?>... args) {
        try {
            return this.clazz(storageClass).getMethod(name, args);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    @Override
    public <T> Method getDeclaredMethod(IStorageClass storageClass, String name, Class<?>... args) {
        try {
            return this.clazz(storageClass).getDeclaredMethod(name, args);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    @Override
    public <T> Field getField(IStorageClass storageClass, String name) {
        return this.getField(this.clazz(storageClass), name);
    }

    @Override
    public <T> Field getField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    @Override
    public <T> T get(IStorageFields storageField, Object instance) {
        try {
            return (T) this.field(storageField).get(instance);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    @Override
    public <T> T get(IStorageFields storageField, Class<?> clazz) {
        try {
            return (T) this.field(storageField).get(clazz);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    @Override
    public void set(IStorageFields storageFields, Object instance, Object value) {
        try {
            Field field = this.field(storageFields);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
    }

    @Override
    public void setFinal(IStorageFields storageFields, Object instance, Object value) {
        try {
            Field field = this.field(storageFields);
            field.setAccessible(true);

            Field modifiersField = getModifiersField();
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(instance, value);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
    }

    private Field getModifiersField() throws NoSuchFieldException {
        try {
            return Field.class.getDeclaredField("modifiers");
        }
        catch (NoSuchFieldException e) {
            try {
                Method getDeclaredFields0 = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
                getDeclaredFields0.setAccessible(true);
                Field[] fields = (Field[]) getDeclaredFields0.invoke(Field.class, false);
                for (Field field : fields) {
                    if ("modifiers".equals(field.getName())) {
                        return field;
                    }
                }
            }
            catch (ReflectiveOperationException ex) {
                e.addSuppressed(ex);
            }
            throw e;
        }
    }

    private void registerDefaults() {
        this.registerDefaultClass();
        this.registerDefaultConstructor();
        this.registerDefaultMethod();
        this.registerDefaultField();
    }

    private void registerDefaultClass() {
        ClassFinder classFinder = this.getNmsManager().getClassFinder();

        this.registerClass(CRAFT_WORLD, classFinder.getOBCClass("CraftWorld"));

        this.registerClass(WORLD, classFinder.worldClass("level.World"));
        this.registerClass(ENTITY, classFinder.worldClass("entity.Entity"));
        this.registerClass(ENTITY_PLAYER, classFinder.getNMSClass("server.level.EntityPlayer"));
        this.registerClass(DATA_WATCHER, classFinder.networkClass("syncher.DataWatcher"));
        this.registerClass(DATA_WATCHER$ITEM, classFinder.networkClass("syncher.DataWatcher$Item"));
        this.registerClass(DATA_WATCHER_OBJECT, classFinder.networkClass("syncher.DataWatcherObject"));
        this.registerClass(ENTITY_LIVING, classFinder.worldClass("entity.EntityLiving"));
        this.registerClass(ENTITY_INSENTIENT, classFinder.worldClass("entity.EntityInsentient"));

        this.registerClass(ITEM_STACK, classFinder.worldClass("item.ItemStack"));
        this.registerClass(NETWORK_MANAGER, classFinder.networkClass("NetworkManager"));
        this.registerClass(PLAYER_CONNECTION, classFinder.getNMSClass("server.network.PlayerConnection"));
        this.registerClass(EQUIPMENT_SLOT, classFinder.worldClass("entity.EnumItemSlot"));

        this.registerClass(PACKET, classFinder.protocolClass("Packet"));
        this.registerClass(PACKET_PLAY_OUT_SPAWN_ENTITY, classFinder.protocolClass("game.PacketPlayOutSpawnEntity"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_DESTROY, classFinder.protocolClass("game.PacketPlayOutEntityDestroy"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_METADATA, classFinder.protocolClass("game.PacketPlayOutEntityMetadata"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_TELEPORT, classFinder.protocolClass("game.PacketPlayOutEntityTeleport"));
        this.registerClass(PACKET_PLAY_OUT_REL_ENTITY_MOVE, classFinder.protocolClass("game.PacketPlayOutEntity$PacketPlayOutRelEntityMove"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_LOOK, classFinder.protocolClass("game.PacketPlayOutEntity$PacketPlayOutEntityLook"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION, classFinder.protocolClass("game.PacketPlayOutEntityHeadRotation"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_VELOCITY, classFinder.protocolClass("game.PacketPlayOutEntityVelocity"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_EQUIPMENT, classFinder.protocolClass("game.PacketPlayOutEntityEquipment"));
        this.registerClass(PACKET_PLAY_OUT_OPEN_SCREEN, classFinder.protocolClass("game.PacketPlayOutOpenWindow"));

        this.registerClass(ADVENTURE_COMPONENT, classFinder.getAbsoluteClass("io.papermc.paper.adventure.AdventureComponent"));

        this.registerClass(VEC_3D, classFinder.worldClass("phys.Vec3D"));
        this.registerClass(BLOCK_POSITION, classFinder.coreClass("BlockPosition"));
        this.registerClass(CHUNK_COORD_INT_PAIR, classFinder.worldClass("level.ChunkCoordIntPair"));
        this.registerClass(SECTION_POSITION, classFinder.coreClass("SectionPosition"));
    }

    private void registerDefaultConstructor() {
        this.registerConstructor(PACKET_PLAY_OUT_SPAWN_ENTITY_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_SPAWN_ENTITY, this.clazz(ENTITY)));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_DESTROY, int[].class));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_METADATA_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_METADATA, int.class, List.class));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_TELEPORT, this.clazz(ENTITY)));
        this.registerConstructor(PACKET_PLAY_OUT_REL_ENTITY_MOVE_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_REL_ENTITY_MOVE, int.class, short.class, short.class, short.class, boolean.class));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_LOOK_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_LOOK, int.class, byte.class, byte.class, boolean.class));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION, this.clazz(ENTITY), byte.class));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_VELOCITY_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_VELOCITY, int.class, Vec3D.class));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_EQUIPMENT, int.class, List.class));
        this.registerConstructor(PACKET_PLAY_OUT_OPEN_SCREEN_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_OPEN_SCREEN, int.class, Containers.class, IChatBaseComponent.class));

        this.registerConstructor(ADVENTURE_COMPONENT_CONSTRUCTOR, this.getConstructor(ADVENTURE_COMPONENT, Component.class));

        this.registerConstructor(VEC_3D_XYZ_CONSTRUCTOR, this.getConstructor(VEC_3D, double.class, double.class, double.class));
        this.registerConstructor(BLOCK_POSITION_XYZ_CONSTRUCTOR, this.getConstructor(BLOCK_POSITION, int.class, int.class, int.class));
        this.registerConstructor(CHUNK_COORD_INT_PAIR_BLOCK_POSITION_CONSTRUCTOR, this.getConstructor(CHUNK_COORD_INT_PAIR, this.clazz(BLOCK_POSITION)));
    }

    private void registerDefaultMethod() {
        this.registerMethod(GET_DATA_WATCHER, this.getMethod(ENTITY, "aj"));
        this.registerMethod(DATA_WATCHER$SET, this.getMethod(DATA_WATCHER, "b", this.clazz(DATA_WATCHER_OBJECT), Object.class));
        this.registerMethod(PACK_DIRTY, this.getMethod(DATA_WATCHER, "b"));

        this.registerMethod(GET_ID, this.getMethod(ENTITY, "hashCode"));
        this.registerMethod(SET_LOCATION, this.getMethod(ENTITY, "b", double.class, double.class, double.class, float.class, float.class));
        this.registerMethod(START_RIDING, this.getMethod(ENTITY, "a", this.clazz(ENTITY), boolean.class));
        this.registerMethod(STOP_RIDING, this.getMethod(ENTITY, "bz"));
        this.registerMethod(GET_PASSENGERS, this.getMethod(ENTITY, "cM"));
        this.registerMethod(TICK, this.getMethod(ENTITY, "l"));
        this.registerMethod(SET_CUSTOM_NAME, this.getMethod(ENTITY, "b", IChatBaseComponent.class));
        this.registerMethod(SET_CUSTOM_NAME_VISIBLE, this.getMethod(ENTITY, "n", boolean.class));

        this.registerMethod(SET_GLOWING, this.getMethod(ENTITY, "i", boolean.class));

        this.registerMethod(SET_INVISIBLE, this.getMethod(ENTITY, "j", boolean.class));
        this.registerMethod(IS_INVISIBLE, this.getMethod(ENTITY, "ca"));

        this.registerMethod(SET_NO_GRAVITY, this.getMethod(ENTITY, "e", boolean.class));
        this.registerMethod(IS_NO_GRAVITY, this.getMethod(ENTITY, "aP"));

        this.registerMethod(AI_STEP, this.getMethod(ENTITY_LIVING, "b_"));
        this.registerMethod(SET_ITEM_SLOT, this.getMethod(ENTITY_LIVING, "a", this.clazz(EQUIPMENT_SLOT), this.clazz(ITEM_STACK)));

        this.registerMethod(SET_NO_AI, this.getMethod(ENTITY_INSENTIENT, "t", boolean.class));
        this.registerMethod(IS_NO_AI, this.getMethod(ENTITY_INSENTIENT, "fK"));

        this.registerMethod(SET_SHIFT_KEY_DOWN, this.getMethod(ENTITY, "f", boolean.class));
        this.registerMethod(IS_SHIFT_KEY_DOWN, this.getMethod(ENTITY, "bO"));

        this.registerMethod(PLAYER_CONNECTION$SEND_PACKET, this.getMethod(PLAYER_CONNECTION, "a", this.clazz(PACKET)));

        this.registerMethod(ADVENTURE_COMPONENT$GET_COMPONENT, this.getMethod(ADVENTURE_COMPONENT, "adventure$component"));

        this.registerMethod(SET_CAMERA, this.getMethod(ENTITY_PLAYER, "c", this.clazz(ENTITY)));

        this.registerMethod(BLOCK_POSITION$GET_X, this.getMethod(BLOCK_POSITION, "u"));
        this.registerMethod(BLOCK_POSITION$GET_Y, this.getMethod(BLOCK_POSITION, "v"));
        this.registerMethod(BLOCK_POSITION$GET_Z, this.getMethod(BLOCK_POSITION, "w"));
    }

    private void registerDefaultField() {
        this.registerField(MDP_ITEMS_BY_ID, this.getField(DATA_WATCHER, "e"));
        this.registerField(NETWORK_MANAGER$CHANNEL, this.getField(NETWORK_MANAGER, "m"));
        this.registerField(PLAYER_HANDLER$PLAYER_CONNECTION, this.getField(ENTITY_PLAYER, "b"));
        this.registerField(PLAYER_CONNECTION$NETWORK_MANAGER, this.getField(PLAYER_CONNECTION, "h"));

        this.registerField(ENTITY$POSITION, this.getField(ENTITY, "t"));
        this.registerField(ENTITY$BLOCK_POSITION, this.getField(ENTITY, "u"));
        this.registerField(ENTITY$CHUNK_POSITION, this.getField(ENTITY, "aC"));

        this.registerField(CHUNK_POSITION$X, this.getField(CHUNK_COORD_INT_PAIR, "e"));
        this.registerField(CHUNK_POSITION$Z, this.getField(CHUNK_COORD_INT_PAIR, "f"));

        this.registerField(TP_PACKET$X, this.getField(PACKET_PLAY_OUT_ENTITY_TELEPORT, "b"));
        this.registerField(TP_PACKET$Y, this.getField(PACKET_PLAY_OUT_ENTITY_TELEPORT, "c"));
        this.registerField(TP_PACKET$Z, this.getField(PACKET_PLAY_OUT_ENTITY_TELEPORT, "d"));
        this.registerField(TP_PACKET$YAW, this.getField(PACKET_PLAY_OUT_ENTITY_TELEPORT, "e"));
        this.registerField(TP_PACKET$PITCH, this.getField(PACKET_PLAY_OUT_ENTITY_TELEPORT, "f"));
    }

}
