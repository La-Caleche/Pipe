package fr.lacaleche.pipe.common.utils.date;

import fr.lacaleche.pipe.common.utils.date.interfaces.Duration;
import fr.lacaleche.pipe.common.utils.date.interfaces.IParser;
import net.kyori.adventure.text.Component;

import java.time.LocalDateTime;
import java.util.Collection;

public class DurationDate implements Duration {

    private final Collection<IParser> parsers;
    private final String rawInput;
    private LocalDateTime date;

    public DurationDate(String input) {
        this.parsers = DateParser.getParsers(input);
        this.rawInput = input;
        this.date = LocalDateTime.now();

        this.parsers.forEach(iParser -> this.date = iParser.apply(this.date, input));
    }

    @Override
    public Collection<IParser> parsers() {
        return parsers;
    }

    @Override
    public LocalDateTime date() {
        return this.date;
    }

    @Override
    public String rawInput() {
        return this.rawInput;
    }

    @Override
    public Component dateToHumanReadable() {
        return DateParser.toHumanReadable(this.date);
    }

    @Override
    public Component durationToHumanReadable() {
        return DateParser.durationToHumanReadable(this.date);
    }
}
