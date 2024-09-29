package fr.lacaleche.pipe.common.commands.parsers;

import fr.lacaleche.core.commands.parsers.LocaleParser;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeLocale;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.parser.ParserDescriptor;

public class PipeLocaleParser<C> extends LocaleParser<C, PipeLocale> {

    public static <C> @NonNull ParserDescriptor<C, PipeLocale> pipeLocaleParser() {
        return ParserDescriptor.of(new PipeLocaleParser<>(), PipeLocale.class);
    }

}
