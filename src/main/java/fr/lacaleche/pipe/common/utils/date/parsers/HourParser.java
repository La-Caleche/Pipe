package fr.lacaleche.pipe.common.utils.date.parsers;

import fr.lacaleche.pipe.common.utils.date.Formats;

import java.time.LocalDateTime;

public class HourParser extends DefaultParser {

    @Override
    public Formats getFormat() {
        return Formats.HOURS;
    }

    @Override
    public LocalDateTime apply(LocalDateTime ldt, String value) {
        int hour = Integer.parseInt(value.replace(getFormat().parser(), ""));
        return ldt.plusHours(hour);
    }

}
