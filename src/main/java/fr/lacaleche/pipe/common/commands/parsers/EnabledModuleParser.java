package fr.lacaleche.pipe.common.commands.parsers;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.features.interfaces.IFeature;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.utils.CalecheDebug;
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

public class EnabledModuleParser<C> implements ArgumentParser<C, IModule>, BlockingSuggestionProvider.Strings<C> {

    private final boolean moduleLocked;
    private final boolean featureLocked;

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

    public static <C> @NonNull ParserDescriptor<C, IModule> parser() {
        return ParserDescriptor.of(new EnabledModuleParser<>(), IModule.class);
    }

    public EnabledModuleParser() {
        this(false, false);
    }

    public EnabledModuleParser(boolean moduleLocked, boolean featureLocked) {
        this.moduleLocked = moduleLocked;
        this.featureLocked = featureLocked;
    }

    @Override
    public @NonNull ArgumentParseResult<IModule> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();
        try {
            final IModule module = Core.get().getCentralModuleManager().getAnyModule(input);
            if (module == null) {
                return ArgumentParseResult.failure(exception(input, commandContext, "argument.parse.failure.module.not_found"));
            }

            if ((module.locked() && this.moduleLocked) || (module.featuresLocked() && this.featureLocked)) {
                return ArgumentParseResult.failure(exception(input, commandContext, "argument.parse.failure.module.not_allowed"));
            }

            return ArgumentParseResult.success(module);
        } catch (final IllegalArgumentException exception) {
            return ArgumentParseResult.failure(exception(input, commandContext, "argument.parse.failure.feature"));
        }
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput input
    ) {
        return Core.get().getCentralModuleManager().getEnabledModules().stream().map((clazz) -> CalecheDebug.shortPackage(clazz.getName())).toList();
    }
}
