package fr.lacaleche.pipe.common.commands.parsers;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.interfaces.PipeCommandManager;
import fr.lacaleche.pipe.common.i18n.LocaleCaptionProvider;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.exception.parsing.ParserException;

public final class CloudParserException extends ParserException {

    public CloudParserException(@NonNull Class<?> argumentParser, @NonNull CommandContext<?> context, @NonNull Caption errorCaption, @NonNull CaptionVariable... captionVariables) {
        super(null, argumentParser, context, errorCaption, captionVariables);
    }

    public static CloudParserException buildException(@NonNull Class<?> argumentParser, @NonNull CommandContext<?> context, @NonNull Caption errorCaption, @NonNull CaptionVariable... captionVariables) {
        final PipeCommandManager<?> commandManager = Pipe.get().getCommandManager();
        final LocaleCaptionProvider<?> captionProvider = commandManager.getCaptionProvider();
        captionProvider.putCaption(errorCaption);
        return new CloudParserException(argumentParser, context, errorCaption, captionVariables);
    }

}
