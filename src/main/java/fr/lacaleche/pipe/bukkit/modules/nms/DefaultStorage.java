package fr.lacaleche.pipe.bukkit.modules.nms;

import com.mojang.authlib.GameProfile;
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
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.monster.EntityMonster;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        this.registerClass(ENTITY_LIVING, classFinder.worldClass("entity.EntityLiving"));
        this.registerClass(ENTITY_INSENTIENT, classFinder.worldClass("entity.EntityInsentient"));

        this.registerClass(ITEM_STACK, classFinder.worldClass("item.ItemStack"));
        this.registerClass(NETWORK_MANAGER, classFinder.networkClass("NetworkManager"));
        this.registerClass(PLAYER_CONNECTION, classFinder.getNMSClass("server.network.PlayerConnection"));
        this.registerClass(EQUIPMENT_SLOT, classFinder.worldClass("entity.EnumItemSlot"));

        this.registerClass(PACKET_PLAY_OUT_SPAWN_ENTITY, classFinder.protocolClass("game.PacketPlayOutSpawnEntity"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_DESTROY, classFinder.protocolClass("game.PacketPlayOutEntityDestroy"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_METADATA, classFinder.protocolClass("game.PacketPlayOutEntityMetadata"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_TELEPORT, classFinder.protocolClass("game.PacketPlayOutEntityTeleport"));
        this.registerClass(PACKET_PLAY_OUT_REL_ENTITY_MOVE, classFinder.protocolClass("game.PacketPlayOutEntity$PacketPlayOutRelEntityMove"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_LOOK, classFinder.protocolClass("game.PacketPlayOutEntity$PacketPlayOutEntityLook"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION, classFinder.protocolClass("game.PacketPlayOutEntityHeadRotation"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_VELOCITY, classFinder.protocolClass("game.PacketPlayOutEntityVelocity"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_EQUIPMENT, classFinder.protocolClass("game.PacketPlayOutEntityEquipment"));

        this.registerClass(ADVENTURE_COMPONENT, classFinder.getAbsoluteClass("io.papermc.paper.adventure.AdventureComponent"));
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

        this.registerConstructor(ADVENTURE_COMPONENT_CONSTRUCTOR, this.getConstructor(ADVENTURE_COMPONENT, Component.class));
    }

    private void registerDefaultMethod() {
        this.registerMethod(GET_DATA_WATCHER, this.getMethod(ENTITY, "aj"));
        this.registerMethod(PACK_DIRTY, this.getMethod(DATA_WATCHER, "b"));

        this.registerMethod(GET_ID, this.getMethod(ENTITY, "hashCode"));
        this.registerMethod(SET_LOCATION, this.getMethod(ENTITY, "b", double.class, double.class, double.class, float.class, float.class));
        this.registerMethod(SET_INVISIBLE, this.getMethod(ENTITY, "j", boolean.class));
        this.registerMethod(SET_GLOWING, this.getMethod(ENTITY, "i", boolean.class));
        this.registerMethod(START_RIDING, this.getMethod(ENTITY, "a", this.clazz(ENTITY), boolean.class));
        this.registerMethod(STOP_RIDING, this.getMethod(ENTITY, "bz"));
        this.registerMethod(GET_PASSENGERS, this.getMethod(ENTITY, "cM"));
        this.registerMethod(TICK, this.getMethod(ENTITY, "l"));
        this.registerMethod(SET_CUSTOM_NAME, this.getMethod(ENTITY, "b", IChatBaseComponent.class));
        this.registerMethod(SET_CUSTOM_NAME_VISIBLE, this.getMethod(ENTITY, "n", boolean.class));
        this.registerMethod(SET_NO_GRAVITY, this.getMethod(ENTITY, "e", boolean.class));
        this.registerMethod(IS_NO_GRAVITY, this.getMethod(ENTITY, "aP"));

        this.registerMethod(AI_STEP, this.getMethod(ENTITY_LIVING, "b_"));
        this.registerMethod(SET_ITEM_SLOT, this.getMethod(ENTITY_LIVING, "a", this.clazz(EQUIPMENT_SLOT), this.clazz(ITEM_STACK)));

        this.registerMethod(SET_NO_AI, this.getMethod(ENTITY_INSENTIENT, "t", boolean.class));
        this.registerMethod(IS_NO_AI, this.getMethod(ENTITY_INSENTIENT, "fK"));

        this.registerMethod(ADVENTURE_COMPONENT$GET_COMPONENT, this.getMethod(ADVENTURE_COMPONENT, "adventure$component"));

        this.registerMethod(SET_CAMERA, this.getMethod(ENTITY_PLAYER, "c", this.clazz(ENTITY)));
    }

    private void registerDefaultField() {
        this.registerField(MDP_ITEMS_BY_ID, this.getField(DATA_WATCHER, "e"));
        this.registerField(NETWORK_MANAGER$CHANNEL, this.getField(NETWORK_MANAGER, "m"));
        this.registerField(PLAYER_CONNECTION$NETWORK_MANAGER, this.getField(PLAYER_CONNECTION, "h"));
    }

}
