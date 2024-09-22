package fr.lacaleche.pipe.common.commands.interfaces;

import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface PipeArgumentsCommandManager<C> {

    BiFunction<Class<?>, String, ?> getArgumentParser(Class<?> type);

    Function<Class<?>, BlockingSuggestionProvider.Strings<C>> getSuggestionProvider(Class<?> type);

}
