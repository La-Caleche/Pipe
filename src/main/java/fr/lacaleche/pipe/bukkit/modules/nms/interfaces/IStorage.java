package fr.lacaleche.pipe.bukkit.modules.nms.interfaces;

import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageFields;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface IStorage {

    Class<?> clazz(StorageClass storageClass);

    Constructor<?> constructor(StorageConstructor storageConstructor);

    Method method(StorageMethods storageMethod);

    Field field(StorageFields storageField);

    void registerClass(StorageClass storageClass, Class<?> clazz);

    void registerConstructor(StorageConstructor storageConstructor, Constructor<?> constructor);

    void registerMethod(StorageMethods storageMethod, Method method);

    void registerField(StorageFields storageField, Field field);

    NMSManager getNmsManager();

    <T> T invoke(StorageMethods storageMethod, Object instance, Object... args);

    <T> T construct(StorageConstructor storageConstructor, Object... args);

    <T> T cast(StorageClass storageClass, Object instance);

    <T> T handle(Object objet);

    <T> T get(StorageFields storageField, Object instance);

    <T> T get(StorageFields storageField, Class<?> clazz);

    void set(StorageFields storageFields, Object instance, Object value);

    <T> Constructor<T> getConstructor(StorageClass storageClass, Class<?>... args);

    <T> Method getMethod(StorageClass storageClass, String name, Class<?>... args);

    <T> Field getField(StorageClass storageClass, String name);

    <T> Field getField(Class<?> clazz, String name);


}
