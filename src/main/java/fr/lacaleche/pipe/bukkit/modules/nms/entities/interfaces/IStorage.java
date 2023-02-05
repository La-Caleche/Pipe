package fr.lacaleche.pipe.bukkit.modules.nms.entities.interfaces;

import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public interface IStorage {

    Class<?> clazz(StorageClass storageClass);

    Constructor<?> constructor(StorageConstructor storageConstructor);

    Method method(StorageMethods storageMethod);

    void registerClass(StorageClass storageClass, Class<?> clazz);

    void registerConstructor(StorageConstructor storageConstructor, Constructor<?> constructor);

    void registerMethod(StorageMethods storageMethod, Method method);

    NMSManager getNmsManager();

    <T> T invoke(StorageMethods storageMethod, Object instance, Object... args);

    <T> T construct(StorageConstructor storageConstructor, Object... args);

    <T> T cast(StorageClass storageClass, Object instance);

    <T> T handle(Object objet);

    <T> Constructor<T> getConstructor(StorageClass storageClass, Class<?>... args);

    <T> Method getMethod(StorageClass storageClass, String name, Class<?>... args);

}
