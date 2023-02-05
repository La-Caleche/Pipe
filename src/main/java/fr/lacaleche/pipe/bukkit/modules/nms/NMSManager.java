package fr.lacaleche.pipe.bukkit.modules.nms;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheEntity;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.interfaces.IStorage;
import fr.lacaleche.pipe.bukkit.modules.nms.utils.NMSFinder;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NMSManager {

    private NMSFinder nmsFinder;

    private final Map<Class<? extends IStorage>, IStorage> storagesCache;

    public NMSManager() {
        this.nmsFinder = new NMSFinder();

        this.storagesCache = new HashMap<>();
    }

    public NMSFinder getNmsFinder() {
        return nmsFinder;
    }

    public <T extends CalecheEntity> T createEntity(Class<? extends CalecheEntity> entityClass, Location location) {
        try {
            Constructor<? extends T> constructor = (Constructor<? extends T>) entityClass.getConstructor(NMSManager.class, Location.class);
            return constructor.newInstance(this, location);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }

        return null;
    }

    public IStorage getStorage(Class<? extends IStorage> storageClass) {
        if (this.storagesCache.containsKey(storageClass)) {
            return this.storagesCache.get(storageClass);
        }

        try {
            Constructor<? extends IStorage> constructor = storageClass.getConstructor(NMSManager.class);
            IStorage storage = constructor.newInstance(this);
            this.storagesCache.put(storageClass, storage);
            return storage;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }

        return null;
    }

    public void sendPacket(Player player, Object packet) {
        try {

            Object handler = this.getNmsFinder().getHandle(player);
            assert handler != null;

            Object connection = handler.getClass().getField("b").get(handler);
            assert connection != null;

            Method sendPackage =  connection.getClass().getMethod("a", this.getNmsFinder().protocolClass("Packet"));

            sendPackage.invoke(connection, packet);
        } catch (NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
    }

}
