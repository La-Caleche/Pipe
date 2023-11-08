package fr.lacaleche.pipe.bukkit.modules.nms;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageFields;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods;
import fr.lacaleche.pipe.bukkit.modules.nms.impls.AbstractController;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import fr.lacaleche.pipe.bukkit.modules.nms.utils.ClassFinder;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
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
        PlayerConnection connection = this.getPlayerConnection(player);
        this.getStorage().invoke(StorageMethods.PLAYER_CONNECTION$SEND_PACKET, connection, packet);
    }

    public PlayerConnection getPlayerConnection(Player player) {
        EntityPlayer handler = this.getPlayerHandle(player);
        return this.getStorage().get(StorageFields.PLAYER_HANDLER$PLAYER_CONNECTION, handler);
    }

    public EntityPlayer getPlayerHandle(Player player) {
        try {
            return (EntityPlayer) this.getClassFinder().getHandle(player);
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
