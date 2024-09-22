package fr.lacaleche.pipe.common.commands.parsers;

import fr.lacaleche.core.modules.features.interfaces.IFeature;
import fr.lacaleche.core.modules.interfaces.IModule;
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

public class FeatureParser<C> implements ArgumentParser<C, IFeature>, BlockingSuggestionProvider.Strings<C> {

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

    public static <C> @NonNull ParserDescriptor<C, IFeature> parser() {
        return ParserDescriptor.of(new FeatureParser<>(), IFeature.class);
    }

    @Override
    public @NonNull ArgumentParseResult<IFeature> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();
        try {
            final IModule inputModule = commandContext.get("module");
            if (inputModule.getFeatureManager() == null)
                return ArgumentParseResult.failure(exception(input, commandContext, "argument.parse.failure.feature.no_feature_manager"));
            final IFeature<?> feature = inputModule.getFeatureManager().getFeatureByName(input);
            return ArgumentParseResult.success(feature);
        } catch (final IllegalArgumentException exception) {
            return ArgumentParseResult.failure(exception(input, commandContext, "argument.parse.failure.feature"));
        }
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput input
    ) {
        final IModule inputModule = commandContext.get("module");
        if (inputModule.getFeatureManager() == null) return Collections.emptyList();
        return inputModule.getFeatureManager().getFeatures().stream().map(IFeature::name).toList();
    }
}
