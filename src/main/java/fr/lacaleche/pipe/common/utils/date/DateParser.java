package fr.lacaleche.pipe.common.utils.date;

import fr.lacaleche.pipe.common.utils.date.interfaces.IParser;
import fr.lacaleche.pipe.common.utils.date.parsers.*;
import net.kyori.adventure.text.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DateParser {

    public static final Map<Formats, IParser> DATE_PARSERS = new HashMap<>();

    static {
        DATE_PARSERS.putIfAbsent(Formats.DAYS, new DayParser());
        DATE_PARSERS.putIfAbsent(Formats.DEF, new DefinitiveParser());
        DATE_PARSERS.putIfAbsent(Formats.HOURS, new HourParser());
        DATE_PARSERS.putIfAbsent(Formats.MINUTES, new MinuteParser());
        DATE_PARSERS.putIfAbsent(Formats.MONTHS, new MonthParser());
        DATE_PARSERS.putIfAbsent(Formats.SECONDS, new SecondParser());
        DATE_PARSERS.putIfAbsent(Formats.DATE, new SpecificDateParser());
        DATE_PARSERS.putIfAbsent(Formats.WEEKS, new WeekParser());
        DATE_PARSERS.putIfAbsent(Formats.YEARS, new YearParser());
    }

    public static Map<Formats, IParser> getParsers() {
        return DATE_PARSERS;
    }

    public static Collection<IParser> getParsers(String input) {
        return getParsers().values().stream().filter(parser -> parser.match(input))
                .toList();
    }

    public static IParser getParser(Formats formats) {
        return getParsers().getOrDefault(formats, null);
    }

    public static Component toHumanReadable(LocalDateTime date) {
        return Component.text("");
    }

    public static Component durationToHumanReadable(LocalDateTime date) {
        return Component.text("");
    }

}

