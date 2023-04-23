package fr.lacaleche.pipe.bukkit.modules.nms.utils;

import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ClassFinder {

    private final String version;
    private final Map<String, Class> cache;

    public ClassFinder() {
        String[] versionArray = Bukkit.getServer().getClass().getName().replace('.', ',').split(",");
        this.version = versionArray.length >= 4 ? versionArray[3] : "";

        this.cache = new HashMap();
    }

    public Class<?> getNMSClass(String className) {
        if (this.cache.containsKey(className)) return this.cache.get(className);

        return this.findWithoutVersion("NMS", "net.minecraft", className);
    }

    public Class<?> worldClass(String className) {
        if (this.cache.containsKey(className)) return this.cache.get(className);

        return this.findWithoutVersion("NMS", "net.minecraft.world", className);
    }

    public Class<?> networkClass(String className) {
        if (this.cache.containsKey(className)) return this.cache.get(className);

        return this.findWithoutVersion("NMS", "net.minecraft.network", className);
    }

    public Class<?> coreClass(String className) {
        if (this.cache.containsKey(className)) return this.cache.get(className);

        return this.findWithoutVersion("NMS", "net.minecraft.core", className);
    }

    public Class<?> protocolClass(String className) {
        if (this.cache.containsKey(className)) return this.cache.get(className);

        return this.findWithoutVersion("NMS", "net.minecraft.network.protocol", className);
    }

    public Class<?> getOBCClass(String className) {
        if (this.cache.containsKey(className)) return this.cache.get(className);

        return this.find("OBC", "org.bukkit.craftbukkit", className);
    }

    public Object getHandle(Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return object.getClass().getMethod("getHandle").invoke(object);
    }

    public Class<?> getAbsoluteClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            this.cache.put(className, clazz);
            return clazz;
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("An error occurred while finding %s class.".formatted(className), exception);
        }
    }

    public Class<?> findWithoutVersion(String type, String unversionedPackage, String className) {
        return this.getAbsoluteClass("%s.%s".formatted(unversionedPackage, className));
    }

    private Class<?> find(String type, String unversionedPackage, String className) {
        return this.getAbsoluteClass("%s.%s.%s".formatted(unversionedPackage, this.version, className));
    }

}
