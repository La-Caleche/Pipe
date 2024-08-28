package fr.lacaleche.pipe.common.commands.parsers;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
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

public class ClientParser<C> implements ArgumentParser<C, Client>, BlockingSuggestionProvider.Strings<C> {

    public static <C> @NonNull ParserDescriptor<C, Client> parser() {
        return ParserDescriptor.of(new ClientParser<>(), Client.class);
    }

    @Override
    public @NonNull ArgumentParseResult<Client> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();
        try {
            final Client client = new ModelFilter<ClientImpl>()
                    .model(ClientImpl.class)
                    .cache(cached -> cached.getUsername().equalsIgnoreCase(input))
                    .sql(sql -> sql.where("username", input))
                    .getOneOrThrow(() -> new IllegalArgumentException("Client not found"));
            return ArgumentParseResult.success(client);
        } catch (final IllegalArgumentException exception) {
            return ArgumentParseResult.failure(new ClientParser.ClientParseException(input, commandContext));
        }
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput input
    ) {
        return Client.allClientUsernames()
                .filter(name -> input.remainingInput().isBlank() || name.startsWith(input.remainingInput()))
                .collect(Collectors.toList());
    }

    public static final class ClientParseException extends ParserException {

        private final String input;

        /**
         * Construct a new MaterialParseException
         *
         * @param input   Input
         * @param context Command context
         */
        public ClientParseException(
                final @NonNull String input,
                final @NonNull CommandContext<?> context
        ) {
            super(
                    ClientParser.class,
                    context,
                    Caption.of("argument.parse.failure.client"),
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
