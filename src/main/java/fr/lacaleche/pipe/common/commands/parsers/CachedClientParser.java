package fr.lacaleche.pipe.common.commands.parsers;

import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.common.clients.Client;
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

import java.util.Collection;
import java.util.stream.Collectors;

public class CachedClientParser<C> implements ArgumentParser<C, Client>, BlockingSuggestionProvider.Strings<C> {

    public static <C> @NonNull ParserDescriptor<C, Client> parser() {
        return ParserDescriptor.of(new CachedClientParser<>(), Client.class);
    }

    @Override
    public @NonNull ArgumentParseResult<Client> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();
        final Collection<Client> cachedClients = Core.get().getModelManager().get(Client.class);
        try {
            final Client client = cachedClients.stream()
                    .filter(cachedClient -> cachedClient.getUsername().equalsIgnoreCase(input))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Client not found"));
            return ArgumentParseResult.success(client);
        } catch (final IllegalArgumentException exception) {
            return ArgumentParseResult.failure(new CachedClientParser.CachedClientParseException(input, commandContext, "notfound"));
        }
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput input
    ) {
        return Core.get().getModelManager().get(Client.class)
                .stream()
                .map(Client::getUsername)
                .filter(name -> input.remainingInput().isBlank() || name.startsWith(input.remainingInput()))
                .collect(Collectors.toList());
    }

    public static final class CachedClientParseException extends ParserException {

        private final String input;

        /**
         * Construct a new MaterialParseException
         *
         * @param input   Input
         * @param context Command context
         */
        public CachedClientParseException(
                final @NonNull String input,
                final @NonNull CommandContext<?> context,
                final @NonNull String caption
        ) {
            super(
                    CachedClientParser.class,
                    context,
                    Caption.of("argument.parse.failure.cachedclient." + caption),
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
