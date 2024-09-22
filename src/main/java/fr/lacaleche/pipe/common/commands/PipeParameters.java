package fr.lacaleche.pipe.common.commands;

import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.parser.ParserParameter;

public class PipeParameters {

    public static final ParserParameter<Boolean> MODULE_LOCKED = create("module-locked", TypeToken.get(Boolean.class));
    public static final ParserParameter<Boolean> FEATURE_LOCKED = create("feature-locked", TypeToken.get(Boolean.class));

    private PipeParameters() {
    }

    private static <T> @NonNull ParserParameter<T> create(
            final @NonNull String key,
            final @NonNull TypeToken<T> expectedType
    ) {
        return new ParserParameter<>(key, expectedType);
    }

}
