package fr.lacaleche.pipe;

import fr.lacaleche.pipe.common.CommonPipe;
import fr.lacaleche.pipe.common.adventure.PipeText;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeClient;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeLocale;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Pipe {

    /**
     * TODO
     */
    PipeLocale getDefaultLocale();

    /**
     * TODO
     */
    @NonNull
    PipeLocale getLocale(PipeClient<?> recipient);

    /**
     * TODO
     */
    PipeText text();

    static Pipe getCommon() {
        return CommonPipe.get();
    }

    static Pipe get() {
        return AbstractPipe.get();
    }

}
