package fr.lacaleche.pipe.bukkit.modules.nms.entities;

import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.interfaces.IStorage;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods.*;

import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods;
import fr.lacaleche.pipe.bukkit.modules.nms.utils.NMSFinder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DefaultStorage implements IStorage {

    private final NMSManager nmsManager;

    private final Map<StorageClass, Class<?>> clazz;
    private final Map<StorageConstructor, Constructor<?>> constructors;
    private final Map<StorageMethods, Method> methods;

    public DefaultStorage(NMSManager nmsManager) {
        this.nmsManager = nmsManager;

        this.clazz = new HashMap<>();
        this.constructors = new HashMap<>();
        this.methods = new HashMap<>();

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
            return (T) this.getNmsManager().getNmsFinder().getHandle(objet);
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

    private void registerDefaults() {
        NMSFinder nmsFinder = this.getNmsManager().getNmsFinder();

        this.registerClass(CRAFT_WORLD, nmsFinder.getOBCClass("CraftWorld"));

        this.registerClass(WORLD, nmsFinder.worldClass("level.World"));
        this.registerClass(ENTITY, nmsFinder.worldClass("entity.Entity"));
        this.registerClass(DATA_WATCHER, nmsFinder.networkClass("syncher.DataWatcher"));
        this.registerClass(ENTITY_LIVING, nmsFinder.worldClass("entity.EntityLiving"));
        this.registerClass(ITEM_STACK, nmsFinder.worldClass("item.ItemStack"));

        this.registerClass(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING, nmsFinder.protocolClass("game.PacketPlayOutSpawnEntityLiving"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_DESTROY, nmsFinder.protocolClass("game.PacketPlayOutEntityDestroy"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_METADATA, nmsFinder.protocolClass("game.PacketPlayOutEntityMetadata"));
        this.registerClass(PACKET_PLAY_OUT_ENTITY_TELEPORT, nmsFinder.protocolClass("game.PacketPlayOutEntityTeleport"));

        this.registerConstructor(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING, this.clazz(ENTITY_LIVING)));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_DESTROY, int[].class));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_METADATA_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_METADATA, int.class, this.clazz(DATA_WATCHER), boolean.class));
        this.registerConstructor(PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR, this.getConstructor(PACKET_PLAY_OUT_ENTITY_TELEPORT, this.clazz(ENTITY)));

        this.registerMethod(SET_LOCATION, this.getMethod(ENTITY, "a", double.class, double.class, double.class, float.class, float.class));
        this.registerMethod(GET_DATA_WATCHER, this.getMethod(ENTITY, "ai"));
        this.registerMethod(GET_ID, this.getMethod(ENTITY, "hashCode"));

    }

}
