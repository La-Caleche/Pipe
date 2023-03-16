package fr.lacaleche.pipe.common.commands.argument;

import fr.lacaleche.pipe.common.commands.argument.arguments.BooleanArgument;
import fr.lacaleche.pipe.common.commands.argument.arguments.DoubleArgument;
import fr.lacaleche.pipe.common.commands.argument.arguments.IntegerArgument;
import fr.lacaleche.pipe.common.commands.argument.arguments.StringArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Argument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArgumentManagerImpl implements ArgumentManager {

    private final Class<?> command;
    private final List<Argument> arguments;

    private static final Map<Class<?>, BiFunction<Class<?>, String, ?>> PARSERS = new HashMap<>();
    static {
        PARSERS.put(Boolean.class, (c, s) -> Boolean.valueOf(s));
        PARSERS.put(Byte.class, (c, s) -> Byte.valueOf(s));
        PARSERS.put(Short.class, (c, s) -> Short.valueOf(s));
        PARSERS.put(Integer.class, (c, s) -> Integer.valueOf(s));
        PARSERS.put(Long.class, (c, s) -> Long.valueOf(s));
        PARSERS.put(Float.class, (c, s) -> Float.valueOf(s));
        PARSERS.put(Double.class, (c, s) -> Double.valueOf(s));
        PARSERS.put(Character.class, (c, s) -> s.charAt(0));
        PARSERS.put(Enum.class, (c, s) -> Enum.valueOf((Class<Enum>) c, s));
        PARSERS.put(String.class, (c, s) -> s);
    }

    private static final Map<Class<?>, BiConsumer<Class<?>, Completer>> VALUES = new HashMap<>();
    static {
        VALUES.put(Boolean.class, (clazz, c) -> new BooleanArgument(null).completer(c));
        VALUES.put(Byte.class, (clazz, c) -> new IntegerArgument(null).completer(c));
        VALUES.put(Short.class, (clazz, c) -> new IntegerArgument(null).completer(c));
        VALUES.put(Integer.class, (clazz, c) -> new IntegerArgument(null).completer(c));
        VALUES.put(Long.class, (clazz, c) -> new IntegerArgument(null).completer(c));
        VALUES.put(Float.class, (clazz, c) -> new DoubleArgument(null).completer(c));
        VALUES.put(Double.class, (clazz, c) -> new DoubleArgument(null).completer(c));
        VALUES.put(Character.class, (clazz, c) -> new StringArgument(null).completer(c));
        VALUES.put(String.class, (clazz, c) -> new StringArgument(null).completer(c));
        VALUES.put(Enum.class, (clazz, c) -> c.addAll(Arrays.stream(clazz.getEnumConstants()).map(Object::toString).collect(Collectors.toList())));
    }

    public ArgumentManagerImpl(Class<?> command) {
        this.command = command;
        this.arguments = new ArrayList<>();
    }

    @Override
    public Class<?> getCommand() {
        return this.command;
    }

    @Override
    public List<Argument> getArguments() {
        return this.arguments;
    }

    @Override
    public Argument getArgument(int index) {
        return getArguments().get(index - 1);
    }

    @Override
    public Argument getAbsoluteArgument(int index) {
        return getArguments().get(index);
    }

    @Override
    public Argument getArgument(String key) {
        return arguments.stream().filter(argument -> argument.getKey().equals(key)).findFirst().orElse(null);
    }

    @Override
    public Argument addArgument(Argument argument) {
        this.arguments.add(argument);
        return argument;
    }

    @Override
    public void removeArgument(Argument argument) {
        this.arguments.remove(argument);
    }

    @Override
    public BiFunction<Class<?>, String, ?> getParser(Class<?> type) {
        BiFunction<Class<?>, String, ?> parser = PARSERS.get(type);
        if (parser == null) {
            if (type == Object.class) return null;
            parser = this.getParser(type.getSuperclass());
        }
        return parser;
    }

    @Override
    public BiConsumer<Class<?>, Completer> getValues(Class<?> type) {
        BiConsumer<Class<?>, Completer> consumer = VALUES.get(type);
        if (consumer == null) {
            if (type == Object.class) return null;
            consumer = this.getValues(type.getSuperclass());
        }
        return consumer;
    }
}
