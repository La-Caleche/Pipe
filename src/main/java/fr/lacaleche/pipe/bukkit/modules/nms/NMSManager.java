package fr.lacaleche.pipe.bukkit.modules.nms;

import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.bukkit.modules.nms.impls.AbstractController;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import fr.lacaleche.pipe.bukkit.modules.nms.utils.ClassFinder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSManager {

    private ClassFinder classFinder;
    private IStorage storage;


    public NMSManager() {
        this.classFinder = new ClassFinder();

        this.storage = new DefaultStorage(this);
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

            Object handler = this.getClassFinder().getHandle(player);
            assert handler != null;

            Object connection = handler.getClass().getField("b").get(handler);
            assert connection != null;

            Method sendPackage =  connection.getClass().getMethod("a", this.getClassFinder().protocolClass("Packet"));

            sendPackage.invoke(connection, packet);
        } catch (NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
    }

    public IStorage getStorage() {
        return storage;
    }
}
