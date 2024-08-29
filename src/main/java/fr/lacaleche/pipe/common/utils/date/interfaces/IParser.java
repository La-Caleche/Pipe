package fr.lacaleche.pipe.common.utils.date.interfaces;

import fr.lacaleche.pipe.common.utils.date.Formats;

import java.time.LocalDateTime;

public interface IParser {
    Formats getFormat();

    LocalDateTime apply(LocalDateTime ldt, String value);

    boolean match(String value);
}
