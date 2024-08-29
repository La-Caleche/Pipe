package fr.lacaleche.pipe.common.utils.date.parsers;

import fr.lacaleche.pipe.common.utils.date.Formats;

import java.time.LocalDateTime;

public class SecondParser extends DefaultParser {

    @Override
    public Formats getFormat() {
        return Formats.SECONDS;
    }

    @Override
    public LocalDateTime apply(LocalDateTime ldt, String value) {
        int second = Integer.parseInt(value.replace(getFormat().parser(), ""));
        return ldt.plusSeconds(second);
    }

}

