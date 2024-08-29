package fr.lacaleche.pipe.common.commands.parsers;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.pipe.common.clients.ranks.RankImpl;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Rank;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

import java.util.stream.Collectors;

public class RankParser<C> implements ArgumentParser<C, Rank>, BlockingSuggestionProvider.Strings<C> {

    protected static CloudParserException exception(
            final @NonNull String input,
            final @NonNull CommandContext<?> context
    ) {
        return CloudParserException.buildException(
                LocaleParser.class,
                context,
                Caption.of("argument.parse.failure.rank"),
                CaptionVariable.of("input", input)
        );
    }

    public static <C> @NonNull ParserDescriptor<C, Rank> parser() {
        return ParserDescriptor.of(new RankParser<>(), Rank.class);
    }

    @Override
    public @NonNull ArgumentParseResult<Rank> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();
        try {
            final Rank rank = new ModelFilter<RankImpl>()
                    .model(RankImpl.class)
                    .cache(cached -> cached.getSlug().equalsIgnoreCase(input))
                    .sql(sql -> sql.where("slug", input))
                    .getOneOrThrow(() -> new IllegalArgumentException("Rank not found"));
            return ArgumentParseResult.success(rank);
        } catch (final IllegalArgumentException exception) {
            return ArgumentParseResult.failure(exception(input, commandContext));
        }
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput input
    ) {
        return new ModelFilter<RankImpl>()
                .model(RankImpl.class)
                .cache(rank -> input.remainingInput().isBlank() || rank.getSlug().startsWith(input.remainingInput()))
                .getAll()
                .map(Rank::getSlug)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

}
