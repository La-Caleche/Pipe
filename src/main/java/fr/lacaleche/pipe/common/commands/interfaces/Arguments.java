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

    int getInt(String key);

    double getDouble(String key);

    boolean getBoolean(String key);

    long getLong(String key);

    float getFloat(String key);

    short getShort(String key);

    byte getByte(String key);

    char getChar(String key);

    Object getObject(String key);

    Object forFeature(String key, IFeature feature);

    <T> T as(String key);

    void forEach(BiConsumer<String, Argument> action);

}
