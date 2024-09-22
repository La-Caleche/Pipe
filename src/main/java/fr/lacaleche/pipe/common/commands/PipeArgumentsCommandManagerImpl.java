package fr.lacaleche.pipe.common.commands;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.common.commands.interfaces.PipeArgumentsCommandManager;
import fr.lacaleche.pipe.common.commands.parsers.typed.PipeDoubleParser;
import fr.lacaleche.pipe.common.commands.parsers.typed.PipeFloatParser;
import org.incendo.cloud.parser.standard.*;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PipeArgumentsCommandManagerImpl<C> implements PipeArgumentsCommandManager<C> {

    private final Map<Class<?>, BiFunction<Class<?>, String, ?>> PARSERS = new HashMap<>();
    private void initParsers() {
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

    private final Map<Class<?>, Function<Class<?>, BlockingSuggestionProvider.Strings<C>>> SUGGESTIONS = new HashMap<>();
    private void initSuggestions() {
        SUGGESTIONS.put(Boolean.class, (clazz) -> new BooleanParser<>(false));
        SUGGESTIONS.put(Byte.class, (clazz) -> new ByteParser<>((byte) -128, (byte) 127));
        SUGGESTIONS.put(Short.class, (clazz) -> new ShortParser<>(Short.MIN_VALUE, (short) 32767));
        SUGGESTIONS.put(Integer.class, (clazz) -> new IntegerParser<>(Integer.MIN_VALUE, Integer.MAX_VALUE));
        SUGGESTIONS.put(Long.class, (clazz) -> new LongParser<>(Long.MIN_VALUE, Long.MAX_VALUE));
        SUGGESTIONS.put(Float.class, (clazz) -> new PipeFloatParser<>());
        SUGGESTIONS.put(Double.class, (clazz) -> new PipeDoubleParser<>());
        SUGGESTIONS.put(Enum.class, (clazz) -> new EnumParser(clazz));
    }

    public PipeArgumentsCommandManagerImpl() {
        this.initParsers();
        this.initSuggestions();
    }

    @Override
    public BiFunction<Class<?>, String, ?> getArgumentParser(Class<?> type) {
        BiFunction<Class<?>, String, ?> parser = PARSERS.get(type);
        if (parser == null) {
            if (type == Object.class) return null;
            parser = this.getArgumentParser(type.getSuperclass());
        }
        return parser;
    }

    @Override
    public Function<Class<?>, BlockingSuggestionProvider.Strings<C>> getSuggestionProvider(Class<?> type) {
        Function<Class<?>, BlockingSuggestionProvider.Strings<C>> function = SUGGESTIONS.get(type);
        if (function == null) {
            if (type == Object.class) return null;
            function = this.getSuggestionProvider(type.getSuperclass());
        }
        return function;
    }

}
