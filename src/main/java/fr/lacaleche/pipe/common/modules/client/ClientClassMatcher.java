package fr.lacaleche.pipe.common.modules.client;

import fr.lacaleche.core.databases.mysql.morph.parser.interfaces.ClassMatcher;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;

public class ClientClassMatcher implements ClassMatcher<Class<? extends ClientImpl>> {

    @Override
    public Class<? extends ClientImpl> match(Class<?> client) {
        if (client == ClientImpl.class || client == Client.class)
            return Pipe.clientClass();
        return null;
    }

}
