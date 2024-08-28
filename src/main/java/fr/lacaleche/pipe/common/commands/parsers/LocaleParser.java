package fr.lacaleche.pipe.common.commands.parsers;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

import java.util.stream.Collectors;

public class LocaleParser<C> implements ArgumentParser<C, Locale>, BlockingSuggestionProvider.Strings<C> {

    public static <C> @NonNull ParserDescriptor<C, Locale> parser() {
        return ParserDescriptor.of(new LocaleParser<>(), Locale.class);
    }

    @Override
    public @NonNull ArgumentParseResult<Locale> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();
        try {
            final Locale locale = new ModelFilter<LocaleImpl>()
                    .model(LocaleImpl.class)
                    .cache(cached -> cached.getSlug().equalsIgnoreCase(input))
                    .sql(sql -> sql.where("slug", input))
                    .getOneOrThrow(() -> new IllegalArgumentException("Locale not found"));
            return ArgumentParseResult.success(locale);
        } catch (final IllegalArgumentException exception) {
            return ArgumentParseResult.failure(new LocaleParser.LocaleParseException(input, commandContext));
        }
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput input
    ) {
        return new ModelFilter<LocaleImpl>()
                .model(LocaleImpl.class)
                .cache(locale -> input.remainingInput().isBlank() || locale.getSlug().startsWith(input.remainingInput()))
                .getAll()
                .map(Locale::getSlug)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    public static final class LocaleParseException extends ParserException {

        private final String input;

        /**
         * Construct a new MaterialParseException
         *
         * @param input   Input
         * @param context Command context
         */
        public LocaleParseException(
                final @NonNull String input,
                final @NonNull CommandContext<?> context
        ) {
            super(
                    ClientParser.class,
                    context,
                    Caption.of("argument.parse.failure.locale"),
                    CaptionVariable.of("input", input)
            );
            this.input = input;
        }

        /**
         * Get the input
         *
         * @return Input
         */
        public @NonNull String input() {
            return this.input;
        }
    }

}
