package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.pipe.common.clients.ranks.RankImpl;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RankArgument extends DefaultArgument {

    public RankArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        completer.addAll(new ModelFilter<RankImpl>().list(RankImpl.class, null, true).map(RankImpl::getSlug).collect(Collectors.toList()));
    }
}
