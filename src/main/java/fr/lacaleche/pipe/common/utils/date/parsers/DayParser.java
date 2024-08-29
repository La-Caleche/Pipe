package fr.lacaleche.pipe.common.utils.date.parsers;

import fr.lacaleche.pipe.common.utils.date.Formats;

import java.time.LocalDateTime;

public class DayParser extends DefaultParser {

    @Override
    public Formats getFormat() {
        return Formats.DAYS;
    }

    @Override
    public LocalDateTime apply(LocalDateTime ldt, String value) {
        int day = Integer.parseInt(value.replace(getFormat().parser(), ""));
        return ldt.plusDays(day);
    }

}
