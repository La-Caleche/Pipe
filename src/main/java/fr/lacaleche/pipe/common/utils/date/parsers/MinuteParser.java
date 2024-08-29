package fr.lacaleche.pipe.common.utils.date.parsers;

import fr.lacaleche.pipe.common.utils.date.Formats;

import java.time.LocalDateTime;

public class MinuteParser extends DefaultParser {

    @Override
    public Formats getFormat() {
        return Formats.MINUTES;
    }

    @Override
    public LocalDateTime apply(LocalDateTime ldt, String value) {
        int minutes = Integer.parseInt(value.replace(getFormat().parser(), ""));
        return ldt.plusMinutes(minutes);
    }

}
