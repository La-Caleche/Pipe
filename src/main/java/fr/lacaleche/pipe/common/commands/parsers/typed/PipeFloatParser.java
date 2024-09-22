package fr.lacaleche.pipe.common.commands.parsers.typed;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.incendo.cloud.type.range.FloatRange;
import org.incendo.cloud.type.range.Range;

import java.util.*;

public class PipeFloatParser<C> implements BlockingSuggestionProvider.Strings<C> {

    private final FloatRange range;

    public PipeFloatParser() {
        this.range = Range.floatRange(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
    }

    private static final int MAX_SUGGESTIONS_INCREMENT = 10;
    private static final int NUMBER_SHIFT_MULTIPLIER = 10;

    public static @NonNull List<@NonNull String> getSuggestions(final @NonNull FloatRange range, final @NonNull CommandInput input) {
        final Set<Float> numbers = new TreeSet<>();
        final String token = input.peekString();

        try {
            final float inputNum = Float.parseFloat(token.equals("-") ? "-0" : token.isEmpty() ? "0" : token);
            final float inputNumAbsolute = Math.abs(inputNum);

            final float min = range.min();
            final float max = range.max();

            numbers.add(inputNumAbsolute); /* It's a valid number, so we suggest it */
            for (int i = 0; i < MAX_SUGGESTIONS_INCREMENT
                    && (inputNum * NUMBER_SHIFT_MULTIPLIER) + i <= max; i++) {
                numbers.add((inputNumAbsolute * NUMBER_SHIFT_MULTIPLIER) + i);
            }

            final List<String> suggestions = new LinkedList<>();
            for (float number : numbers) {
                if (token.startsWith("-")) {
                    number = -number; /* Preserve sign */
                }
                if (number < min || number > max) {
                    continue;
                }
                suggestions.add(String.valueOf(number));
            }

            return suggestions;
        } catch (final Exception ignored) {
            return Collections.emptyList();
        }
    }

    public @NonNull Iterable<@NonNull String> stringSuggestions(final @NonNull CommandContext<C> commandContext, final @NonNull CommandInput input) {
        return getSuggestions(this.range, input);
    }

}
