package fr.lacaleche.pipe.common.utils.date.parsers;


import fr.lacaleche.pipe.common.utils.date.Formats;

import java.time.LocalDateTime;

public class YearParser extends DefaultParser {

    @Override
    public Formats getFormat() {
        return Formats.YEARS;
    }

    @Override
    public LocalDateTime apply(LocalDateTime ldt, String value) {
        int years = Integer.parseInt(value.replace(getFormat().parser(), ""));
        return ldt.plusYears(years);
    }

}
