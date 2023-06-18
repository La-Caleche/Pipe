package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.pipe.common.clients.ranks.RankImpl;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;

import java.util.stream.Collectors;

public class LangArgument extends DefaultArgument {

    public LangArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        completer.addAll(new ModelFilter<LocaleImpl>().model(LocaleImpl.class).getAll().map(LocaleImpl::getSlug).collect(Collectors.toList()));
    }
}
