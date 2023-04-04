package fr.lacaleche.pipe.common.commands.argument;

import fr.lacaleche.core.modules.features.interfaces.IFeature;
import fr.lacaleche.pipe.common.commands.argument.arguments.StringArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Argument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ArgumentsImpl implements Arguments {

    private ArgumentManager manager;

    public ArgumentsImpl(ArgumentManager manager) {
        this.manager = manager;
    }

    @Override
    public Argument get(String key) {
        return this.manager.getArguments().stream().filter(argument -> argument.getKey().equals(key)).findFirst().orElse(new StringArgument("null"));
    }

    @Override
    public String getString(String key) {
        return this.get(key).getValue();
    }

    @Override
    public int getInt(String key) {
        return Integer.parseInt(this.getString(key));
    }

    @Override
    public double getDouble(String key) {
        return Double.parseDouble(this.getString(key));
    }

    @Override
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(this.getString(key));
    }

    @Override
    public long getLong(String key) {
        return Long.parseLong(this.getString(key));
    }

    @Override
    public float getFloat(String key) {
        return Float.parseFloat(this.getString(key));
    }

    @Override
    public short getShort(String key) {
        return Short.parseShort(this.getString(key));
    }

    @Override
    public byte getByte(String key) {
        return Byte.parseByte(this.getString(key));
    }

    @Override
    public char getChar(String key) {
        return this.getString(key).charAt(0);
    }

    @Override
    public Object getObject(String key) {
        return this.get(key).getValue();
    }

    @Override
    public Object forFeature(String key, IFeature feature) {
        Class<?> type = feature.value().type();
        BiFunction<Class<?>, String, ?> parser = this.manager.getParser(type);
        if (parser == null) return null;
        return parser.apply(type, this.getString(key));
    }

    @Override
    public <T> T as(String key) {
        return (T) this.getObject(key);
    }

    @Override
    public boolean exist(String key) {
        return this.manager.getArguments().stream().anyMatch(argument -> argument.getKey().equals(key));
    }

    @Override
    public boolean blank(String key) {
        return !this.exist(key) ? true : this.get(key).getValue().isBlank();
    }

    @Override
    public boolean mandatory(String key) {
        return this.exist(key) ? manager.getArgument(key).isMandatory() : false;
    }

    @Override
    public void forEach(BiConsumer<String, Argument> action) {
        this.manager.getArguments().forEach(argument -> action.accept(argument.getKey(), argument));
    }
}
