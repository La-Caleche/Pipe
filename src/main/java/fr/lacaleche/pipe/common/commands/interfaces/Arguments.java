package fr.lacaleche.pipe.common.commands.interfaces;

import fr.lacaleche.core.modules.features.interfaces.IFeature;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Argument;

import java.util.function.BiConsumer;

public interface Arguments {

    Argument get(String key);

    boolean exist(String key);

    boolean blank(String key);

    boolean mandatory(String key);

    String getString(String key);

    String getString(String key, String def);

    int getInt(String key);

    int getInt(String key, int def);

    double getDouble(String key);

    double getDouble(String key, double def);

    boolean getBoolean(String key);

    boolean getBoolean(String key, boolean def);

    long getLong(String key);

    long getLong(String key, long def);

    float getFloat(String key);

    float getFloat(String key, float def);

    short getShort(String key);

    short getShort(String key, short def);

    byte getByte(String key);

    byte getByte(String key, byte def);

    char getChar(String key);

    char getChar(String key, char def);

    Object getObject(String key);

    Object getObject(String key, Object def);

    Object forFeature(String key, IFeature feature);

    <T> T as(String key);

    void forEach(BiConsumer<String, Argument> action);

}
