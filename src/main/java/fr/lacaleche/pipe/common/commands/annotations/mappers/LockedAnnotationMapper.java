package fr.lacaleche.pipe.common.commands.annotations.mappers;

import fr.lacaleche.pipe.common.commands.PipeParameters;
import fr.lacaleche.pipe.common.commands.annotations.Locked;
import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.parser.ParserParameter;
import org.incendo.cloud.parser.ParserParameters;
import org.incendo.cloud.parser.ParserRegistry;

public class LockedAnnotationMapper implements ParserRegistry.AnnotationMapper<Locked> {

    @Override
    public @NonNull ParserParameters mapAnnotation(final @NonNull Locked locked, final @NonNull TypeToken<?> typeToken) {
        ParserParameter<Boolean> parameter = switch (locked.value()) {
            case MODULE -> PipeParameters.MODULE_LOCKED;
            case FEATURE -> PipeParameters.FEATURE_LOCKED;
        };
        return ParserParameters.single(parameter, true);
    }
}
