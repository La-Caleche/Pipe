package fr.lacaleche.pipe.common.commands.injectors;

import fr.lacaleche.pipe.common.commands.GlobalPipeCommandContext;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.injection.ParameterInjector;
import org.incendo.cloud.util.annotation.AnnotationAccessor;

public class GlobalPipeCommandContextInjector<C> implements ParameterInjector<C, GlobalPipeCommandContext> {

    @Override
    public @Nullable GlobalPipeCommandContext<C> create(
            final @NonNull CommandContext<C> context,
            final @NonNull AnnotationAccessor annotationAccessor
    ) {
        return new GlobalPipeCommandContext<>(context.sender());
    }

}
