package fr.lacaleche.pipe.common.commands.interfaces;

import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.enums.CommandExecutor;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import net.kyori.adventure.text.Component;

import java.util.Optional;

public interface PipeCommandContext<C> {

    Locale locale();

    Client client();

    <T> Optional<Client> searchClient(T target);

    C sender();

    Client clientOrReject(Client client);

    CommandExecutor executor();

    void sendMessage(String message);

    void sendMessage(Component component);

}
