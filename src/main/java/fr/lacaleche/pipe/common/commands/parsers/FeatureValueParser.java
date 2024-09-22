package fr.lacaleche.pipe.common.commands.parsers;

import fr.lacaleche.core.modules.features.impl.FeatureValue;
import fr.lacaleche.core.modules.features.interfaces.IFeature;
import fr.lacaleche.core.modules.features.interfaces.IFeatureValue;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.interfaces.PipeArgumentsCommandManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FeatureValueParser<C> implements ArgumentParser<C, IFeatureValue>, BlockingSuggestionProvider.Strings<C> {

    protected static CloudParserException exception(
            final @NonNull String input,
            final @NonNull CommandContext<?> context,
            final @NonNull String caption
    ) {
        return CloudParserException.buildException(
                AvailableModulesParser.class,
                context,
                Caption.of(caption),
                CaptionVariable.of("input", input)
        );
    }

    public static <C> @NonNull ParserDescriptor<C, IFeatureValue> parser() {
        return ParserDescriptor.of(new FeatureValueParser<>(), IFeatureValue.class);
    }

    @Override
    public @NonNull ArgumentParseResult<IFeatureValue> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();
        try {
            final IModule module = commandContext.get("module");
            final IFeature<Object> feature = commandContext.get("feature");
            final IFeatureValue<Object> featureValue = (IFeatureValue<Object>) feature.value().clone();

            BiFunction<Class<?>, String, ?> parser = getArgumentCommandManager().getArgumentParser(featureValue.type());
            if (parser == null) return ArgumentParseResult.failure(exception(input, commandContext, "argument.parse.failure.featurevalue.no_parser"));
            featureValue.setValue(parser.apply(featureValue.type(), input));

            return ArgumentParseResult.success(featureValue);
        } catch (final IllegalArgumentException exception) {
            return ArgumentParseResult.failure(exception(input, commandContext, "argument.parse.failure.featurevalue"));
        }
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput input
    ) {
        final IFeature<?> inputFeature = commandContext.get("feature");
        final Function<Class<?>, BlockingSuggestionProvider.Strings<C>> provider = getArgumentCommandManager().getSuggestionProvider(inputFeature.value().type());
        if (provider == null) return Collections.emptyList();
        return provider.apply(inputFeature.value().type()).stringSuggestions(commandContext, input);
    }

    private PipeArgumentsCommandManager<C> getArgumentCommandManager() {
        return (PipeArgumentsCommandManager<C>) Pipe.get().getCommandManager().getArgumentsCommandManager();
    }

}
