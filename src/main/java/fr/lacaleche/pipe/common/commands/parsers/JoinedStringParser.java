package fr.lacaleche.pipe.common.commands.parsers;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class JoinedStringParser<C> implements ArgumentParser<C, String> {

    private static final Pattern FLAG_PATTERN = Pattern.compile("(-[A-Za-z_\\-0-9])|(--[A-Za-z_\\-0-9]*)");

    public static <C> @NonNull ParserDescriptor<C, String> parser() {
        return ParserDescriptor.of(new JoinedStringParser<>(), String.class);
    }

    public static <C> @NonNull ParserDescriptor<C, String> flagYieldingJoinedStringParser() {
        return ParserDescriptor.of(new JoinedStringParser<>(true), String.class);
    }

    private final boolean flagYielding;

    public JoinedStringParser() {
        this.flagYielding = false;
    }

    public JoinedStringParser(final boolean flagYielding) {
        this.flagYielding = flagYielding;
    }

    @Override
    public @NonNull ArgumentParseResult<String> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final int size = commandInput.remainingTokens();

        if (this.flagYielding) {
            final List<String> result = new LinkedList<>();

            for (int i = 0; i < size; i++) {
                final String string = commandInput.peekString();
                if (string.isEmpty() || FLAG_PATTERN.matcher(string).matches()) {
                    break;
                }
                result.add(commandInput.readString());
            }

            return ArgumentParseResult.success(String.join(" ", result));
        } else {
            final String[] result = new String[size];
            for (int i = 0; i < result.length; i++) {
                result[i] = commandInput.readString();
            }
            return ArgumentParseResult.success(String.join(" ", result));
        }
    }

}
