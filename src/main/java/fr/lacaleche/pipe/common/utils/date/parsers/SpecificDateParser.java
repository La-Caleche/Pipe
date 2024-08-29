package fr.lacaleche.pipe.common.utils.date.parsers;

import fr.lacaleche.pipe.common.utils.date.Formats;
import fr.lacaleche.pipe.common.utils.date.interfaces.IParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class SpecificDateParser implements IParser {

    @Override
    public Formats getFormat() {
        return Formats.DATE;
    }

    @Override
    public LocalDateTime apply(LocalDateTime ldt, String value) {
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern(getFormat().getCompleter());
            return LocalDateTime.parse(value, format);
        } catch (DateTimeParseException ignored) {}
        return null;
    }

    @Override
    public boolean match(String value) {
        LocalDateTime date = apply(null, value);
        return date != null;
    }

}

