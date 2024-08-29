package fr.lacaleche.pipe.common.commands.parsers;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

import java.util.Locale;
import java.util.stream.Collectors;

public class ClientParser<C, T extends Client> implements ArgumentParser<C, T>, BlockingSuggestionProvider.Strings<C> {

    protected static CloudParserException exception(
            final @NonNull String input,
            final @NonNull CommandContext<?> context
    ) {
        return CloudParserException.buildException(
                ClientParser.class,
                context,
                Caption.of("argument.parse.failure.client"),
                CaptionVariable.of("input", input)
        );
    }

    public static <C> @NonNull ParserDescriptor<C, Client> clientParser() {
        return ParserDescriptor.of(new ClientParser<>(), Client.class);
    }

    @Override
    public @NonNull ArgumentParseResult<T> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();
        try {
            final Client client = new ModelFilter<ClientImpl>()
                    .model((Class<ClientImpl>) Pipe.clientClass())
                    .cache(cached -> cached.getUsername().equalsIgnoreCase(input))
                    .sql(sql -> sql.where("username", input))
                    .getOneOrThrow(() -> new IllegalArgumentException("Client not found"));
            return (ArgumentParseResult<T>) ArgumentParseResult.success(client);
        } catch (final IllegalArgumentException exception) {
            return ArgumentParseResult.failure(exception(input, commandContext));
        }
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput input
    ) {
        return Client.allClientUsernames()
                .filter(name -> input.remainingInput().isBlank() || name.toLowerCase(Locale.ROOT).startsWith(input.remainingInput().toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }

}
