package fr.lacaleche.pipe.common.commands.parsers;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.utils.CalecheDebug;
import fr.lacaleche.pipe.common.modules.common.ModuleClass;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

public class AvailableModulesParser<C> implements ArgumentParser<C, ModuleClass>, BlockingSuggestionProvider.Strings<C> {

    protected static CloudParserException exception(
            final @NonNull String input,
            final @NonNull CommandContext<?> context
    ) {
        return CloudParserException.buildException(
                AvailableModulesParser.class,
                context,
                Caption.of("argument.parse.failure.availablemodules"),
                CaptionVariable.of("input", input)
        );
    }

    public static <C> @NonNull ParserDescriptor<C, ModuleClass> parser() {
        return ParserDescriptor.of(new AvailableModulesParser<>(), ModuleClass.class);
    }

    @Override
    public @NonNull ArgumentParseResult<ModuleClass> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();
        try {
            final ModuleClass moduleClass = ModuleClass.of(Core.get().getCentralModuleManager()
                    .getAnyAvailableModule(input));
            return ArgumentParseResult.success(moduleClass);
        } catch (final IllegalArgumentException exception) {
            return ArgumentParseResult.failure(exception(input, commandContext));
        }
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput input
    ) {
        return Core.get().getCentralModuleManager().getAvailableModules()
                .stream().map(clazz -> CalecheDebug.shortPackage(clazz.getName())).toList();
    }
}
