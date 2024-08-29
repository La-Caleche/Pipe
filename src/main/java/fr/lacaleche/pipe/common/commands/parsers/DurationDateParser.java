package fr.lacaleche.pipe.common.commands.parsers;

import fr.lacaleche.pipe.common.utils.date.DurationDate;
import fr.lacaleche.pipe.common.utils.date.Formats;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

import java.util.Arrays;

public class DurationDateParser<C> implements ArgumentParser<C, DurationDate>, BlockingSuggestionProvider.Strings<C> {

    protected static CloudParserException exception(
            final @NonNull String input,
            final @NonNull CommandContext<?> context
    ) {
        return CloudParserException.buildException(
                DurationDateParser.class,
                context,
                Caption.of("argument.parse.failure.durationdate"),
                CaptionVariable.of("input", input)
        );
    }

    public static <C> @NonNull ParserDescriptor<C, DurationDate> parser() {
        return ParserDescriptor.of(new DurationDateParser<>(), DurationDate.class);
    }

    @Override
    public @NonNull ArgumentParseResult<DurationDate> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();
        final DurationDate durationDate = new DurationDate(input);

        if (durationDate.parsers().isEmpty())
            return ArgumentParseResult.failure(exception(input, commandContext));

        return ArgumentParseResult.success(durationDate);
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput input
    ) {
        return Arrays.stream(Formats.values()).map(Formats::getCompleter).toList();
    }

}
