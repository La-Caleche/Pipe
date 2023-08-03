package fr.lacaleche.pipe.common.commands.interfaces;

import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;

import javax.annotation.Nullable;

public interface Command<T> {

    T sender();

    Arguments args();

    Locale locale();

    @Nullable
    Client clientSender();

}
