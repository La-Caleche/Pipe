package fr.lacaleche.pipe.bukkit.modules.nms;

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
import fr.lacaleche.pipe.bukkit.modules.nms.utils.ClassFinder;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3D;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultStorage implements IStorage {

    private final NMSManager nmsManager;

    private final Map<StorageClass, Class<?>> clazz;
    private final Map<StorageConstructor, Constructor<?>> constructors;
    private final Map<StorageMethods, Method> methods;
    private final Map<StorageFields, Field> fields;

    public DefaultStorage(NMSManager nmsManager) {
        this.nmsManager = nmsManager;

        this.clazz = new HashMap<>();
        this.constructors = new HashMap<>();
        this.methods = new HashMap<>();
        this.fields = new HashMap<>();

        this.registerDefaults();
    }

    @Override
    public Class<?> clazz(StorageClass storageClass) {
        return this.clazz.getOrDefault(storageClass, null);
    }

    @Override
    public Constructor<?> constructor(StorageConstructor storageConstructor) {
        return this.constructors.getOrDefault(storageConstructor, null);
    }

    @Override
    public Method method(StorageMethods storageMethod) {
        return this.methods.getOrDefault(storageMethod, null);
    }

    @Override
    public Field field(StorageFields storageField) {
        Field field = this.fields.getOrDefault(storageField, null);
        if (field != null) field.setAccessible(true);
        return field;
    }

    @Override
    public void registerClass(StorageClass storageClass, Class<?> clazz) {
        this.clazz.putIfAbsent(storageClass, clazz);
    }

    @Override
    public void registerConstructor(StorageConstructor storageConstructor, Constructor<?> constructor) {
        this.constructors.putIfAbsent(storageConstructor, constructor);
    }

    @Override
    public void registerMethod(StorageMethods storageMethod, Method method) {
        this.methods.putIfAbsent(storageMethod, method);
    }

    @Override
    public void registerField(StorageFields storageField, Field field) {
        this.fields.putIfAbsent(storageField, field);
    }

    @Override
    public NMSManager getNmsManager() {
        return nmsManager;
    }

    @Override
    public <T> T invoke(StorageMethods storageMethod, Object instance, Object... args) {
        try {
            return (T) this.method(storageMethod).invoke(instance, args);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    @Override
    public <T> T construct(StorageConstructor storageConstructor, Object... args) {
        try {
            return (T) this.constructor(storageConstructor).newInstance(args);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    @Override
    public <T> T cast(StorageClass storageClass, Object instance) {
        try {
            return (T) this.clazz(storageClass).cast(instance);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    @Override
    public <T> Constructor<T> getConstructor(StorageClass storageClass, Class<?>... args) {
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
    public <T> Method getMethod(StorageClass storageClass, String name, Class<?>... args) {
        try {
            return this.clazz(storageClass).getMethod(name, args);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    @Override
    public <T> Field getField(StorageClass storageClass, String name) {
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
    public <T> T get(StorageFields storageField, Object instance) {
        try {
            return (T) this.field(storageField).get(instance);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    @Override
    public <T> T get(StorageFields storageField, Class<?> clazz) {
        try {
            return (T) this.field(storageField).get(clazz);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    private void registerDefaults() {
        ClassFinder classFinder = this.getNmsManager().getClassFinder();

        this.registerClass(CRAFT_WORLD, classFinder.getOBCClass("CraftWorld"));

        this.registerClass(WORLD, classFinder.worldClass("level.World"));
        this.registerClass(ENTITY, classFinder.worldClass("entity.Entity"));
        this.registerClass(DATA_WATCHER, classFinder.networkClass("syncher.DataWatcher"));
        this.registerClass(ENTITY_LIVING, classFinder.worldClass("entity.EntityLiving"));
        this.registerClass(ITEM_STACK, classFinder.worldClass("item.ItemStack"));

        this.registerClass(PACKET_PLAY_OUT_SPAWN_ENTITY, classFinder.protocolClass("game.PacketPlayOutSpawnEntity"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_DESTROY, classFinder.protocolClass("game.PacketPlayOutEntityDestroy"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_METADATA, classFinder.protocolClass("game.PacketPlayOutEntityMetadata"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_TELEPORT, classFinder.protocolClass("game.PacketPlayOutEntityTeleport"));
        this.registerClass(PACKET_PLAY_OUT_REL_ENTITY_MOVE, classFinder.protocolClass("game.PacketPlayOutEntity$PacketPlayOutRelEntityMove"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_LOOK, classFinder.protocolClass("game.PacketPlayOutEntity$PacketPlayOutEntityLook"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION, classFinder.protocolClass("game.PacketPlayOutEntityHeadRotation"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_VELOCITY, classFinder.protocolClass("game.PacketPlayOutEntityVelocity"));

        this.registerClass(ADVENTURE_COMPONENT, classFinder.getAbsoluteClass("io.papermc.paper.adventure.AdventureComponent"));

        this.registerConstructor(PACKET_PLAY_OUT_SPAWN_ENTITY_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_SPAWN_ENTITY, this.clazz(ENTITY)));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_DESTROY, int[].class));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_METADATA_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_METADATA, int.class, List.class));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_TELEPORT, this.clazz(ENTITY)));
        this.registerConstructor(PACKET_PLAY_OUT_REL_ENTITY_MOVE_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_REL_ENTITY_MOVE, int.class, short.class, short.class, short.class, boolean.class));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_LOOK_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_LOOK, int.class, byte.class, byte.class, boolean.class));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION, this.clazz(ENTITY), byte.class));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_VELOCITY_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_VELOCITY, int.class, Vec3D.class));

        this.registerConstructor(ADVENTURE_COMPONENT_CONSTRUCTOR, this.getConstructor(ADVENTURE_COMPONENT, Component.class));

        this.registerMethod(GET_DATA_WATCHER, this.getMethod(ENTITY, "aj"));
        this.registerMethod(PACK_DIRTY, this.getMethod(DATA_WATCHER, "b"));
        this.registerMethod(GET_ID, this.getMethod(ENTITY, "hashCode"));
        this.registerMethod(SET_LOCATION, this.getMethod(ENTITY, "b", double.class, double.class, double.class, float.class, float.class));
        this.registerMethod(SET_INVISIBLE, this.getMethod(ENTITY, "j", boolean.class));
        this.registerMethod(SET_GLOWING, this.getMethod(ENTITY, "i", boolean.class));
        this.registerMethod(TICK, this.getMethod(ENTITY_LIVING, "l"));
        this.registerMethod(AI_STEP, this.getMethod(ENTITY_LIVING, "b_"));
        this.registerMethod(SET_CUSTOM_NAME, this.getMethod(ENTITY, "b", IChatBaseComponent.class));
        this.registerMethod(SET_CUSTOM_NAME_VISIBLE, this.getMethod(ENTITY, "n", boolean.class));

        this.registerField(MDP_ITEMS_BY_ID, this.getField(DATA_WATCHER, "e"));
    }

}
