package fr.lacaleche.pipe.common.utils.date.parsers;

import fr.lacaleche.pipe.common.utils.date.Formats;
import fr.lacaleche.pipe.common.utils.date.interfaces.IParser;

import java.time.LocalDateTime;

public class DefinitiveParser implements IParser {

    @Override
    public Formats getFormat() {
        return Formats.DEF;
    }

    @Override
    public LocalDateTime apply(LocalDateTime ldt, String value) {
        return null;
    }

    @Override
    public boolean match(String value) {
        return value.equalsIgnoreCase(getFormat().parser());
    }

}

