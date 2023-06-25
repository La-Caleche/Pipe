package fr.lacaleche.pipe.bukkit.modules.nms.interfaces;

import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.enums.IStorageClass;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.enums.IStorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.enums.IStorageFields;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.enums.IStorageMethods;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface IStorage {

    Class<?> clazz(IStorageClass storageClass);

    Constructor<?> constructor(IStorageConstructor storageConstructor);

    Method method(IStorageMethods storageMethod);

    Field field(IStorageFields storageField);

    void registerClass(IStorageClass storageClass, Class<?> clazz);

    void registerConstructor(IStorageConstructor storageConstructor, Constructor<?> constructor);

    void registerMethod(IStorageMethods storageMethod, Method method);

    void registerField(IStorageFields storageField, Field field);

    NMSManager getNmsManager();

    <T> T invoke(IStorageMethods storageMethod, Object instance, Object... args);

    <T> T construct(IStorageConstructor storageConstructor, Object... args);

    <T> T cast(IStorageClass storageClass, Object instance);

    <T> T handle(Object objet);

    <T> T get(IStorageFields storageField, Object instance);

    <T> T get(IStorageFields storageField, Class<?> clazz);

    void set(IStorageFields storageFields, Object instance, Object value);

    void setFinal(IStorageFields storageFields, Object instance, Object value);

    <T> Constructor<T> getConstructor(IStorageClass storageClass, Class<?>... args);

    <T> Method getMethod(IStorageClass storageClass, String name, Class<?>... args);

    <T> Method getDeclaredMethod(IStorageClass storageClass, String name, Class<?>... args);

    <T> Field getField(IStorageClass storageClass, String name);

    <T> Field getField(Class<?> clazz, String name);


}
