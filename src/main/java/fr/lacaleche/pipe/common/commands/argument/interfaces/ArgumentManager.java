package fr.lacaleche.pipe.common.commands.argument.interfaces;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface ArgumentManager {

    Class<?> getCommand();

    List<Argument> getArguments();

    Argument getArgument(int index);

    Argument getArgument(String key);

    Argument getAbsoluteArgument(int index);

    void removeArgument(Argument argument);

    Argument addArgument(Argument argument);

    BiFunction<Class<?>, String, ?> getParser(Class<?> type);

    BiConsumer<Class<?>, Completer> getValues(Class<?> type);

}
