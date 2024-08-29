package fr.lacaleche.pipe.common.utils.date.interfaces;

import net.kyori.adventure.text.Component;

import java.time.LocalDateTime;
import java.util.Collection;

public interface Duration {

    Collection<IParser> parsers();

    LocalDateTime date();

    String rawInput();

    Component dateToHumanReadable();

    Component durationToHumanReadable();

}
