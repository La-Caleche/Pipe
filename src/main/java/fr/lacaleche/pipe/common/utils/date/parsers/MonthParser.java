package fr.lacaleche.pipe.common.utils.date.parsers;

import fr.lacaleche.pipe.common.utils.date.Formats;

import java.time.LocalDateTime;

public class MonthParser extends DefaultParser {

    @Override
    public Formats getFormat() {
        return Formats.MONTHS;
    }

    @Override
    public LocalDateTime apply(LocalDateTime ldt, String value) {
        int month = Integer.parseInt(value.replace(getFormat().parser(), ""));
        return ldt.plusMonths(month);
    }

}

