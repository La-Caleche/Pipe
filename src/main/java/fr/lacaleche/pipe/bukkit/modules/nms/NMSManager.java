package fr.lacaleche.pipe.bukkit.modules.nms;

import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.bukkit.modules.nms.impls.AbstractController;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import fr.lacaleche.pipe.bukkit.modules.nms.utils.ClassFinder;
import io.netty.channel.Channel;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSManager {

    private ClassFinder classFinder;
    private IStorage storage;


    public NMSManager() {
        this.classFinder = new ClassFinder();

        this.setStorage(new DefaultStorage(this));
    }

    public ClassFinder getClassFinder() {
        return classFinder;
    }

    public <T extends AbstractController> T createEntity(Class<? extends AbstractController> entityClass, Location location) {
        try {
            Constructor<? extends T> constructor = (Constructor<? extends T>) entityClass.getConstructor(NMSManager.class, Location.class);
            return constructor.newInstance(this, location);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }

        return null;
    }

    public void sendPacket(Player player, Object packet) {
        try {
            Object connection = this.getPlayerConnection(player);

            Method sendPacket = connection.getClass().getMethod("a", this.getClassFinder().protocolClass("Packet"));

            sendPacket.invoke(connection, packet);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
    }

    public Object getPlayerConnection(Player player) {
        try {
            Object handler = this.getPlayerHandle(player);
            assert handler != null;

            Object connection = handler.getClass().getField("b").get(handler);
            assert connection != null;

            return connection;
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    public Object getPlayerHandle(Player player) {
        try {
            return this.getClassFinder().getHandle(player);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
        return null;
    }

    protected void setStorage(IStorage storage) {
        this.storage = storage;
    }

    public IStorage getStorage() {
        return storage;
    }
}
