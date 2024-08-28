package fr.lacaleche.pipe.common.commands.interfaces;

import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;

import java.util.Optional;

public interface CommandContext<C> {

    Locale locale();

    Client client();

    <T> Optional<Client> searchClient(T target);

    C sender();

    Client clientOrReject(Client client);

}
