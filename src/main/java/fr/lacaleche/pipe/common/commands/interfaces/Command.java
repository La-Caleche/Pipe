package fr.lacaleche.pipe.common.commands.interfaces;

import fr.lacaleche.pipe.common.i18n.interfaces.Locale;

public interface Command<T> {

    T sender();

    Arguments args();

    Locale locale();

}
