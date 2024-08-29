package fr.lacaleche.pipe.common.utils.date.parsers;

import fr.lacaleche.pipe.common.utils.date.Formats;

import java.time.LocalDateTime;

public class WeekParser extends DefaultParser {

    @Override
    public Formats getFormat() {
        return Formats.WEEKS;
    }

    @Override
    public LocalDateTime apply(LocalDateTime ldt, String value) {
        int weeks = Integer.parseInt(value.replace(getFormat().parser(), ""));
        return ldt.plusWeeks(weeks);
    }

}

