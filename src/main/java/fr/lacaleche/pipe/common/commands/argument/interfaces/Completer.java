package fr.lacaleche.pipe.common.commands.argument.interfaces;

import fr.lacaleche.pipe.common.commands.CoreCommandImpl;

import java.util.Collection;
import java.util.List;

public interface Completer {

    public CoreCommandImpl getCoreCommand();

    public ArgumentManager getArgumentManager();

    public List<String> getCompleter();

    public int index();

    public void incrementIndex();

    public void setIndex(int index);

    public boolean next();

    public void setNext(boolean next);

    public void add(String value);

    public void addAll(Collection<? extends String> values);

    public void setCompleter(List<String> completer);

    public boolean doValidation();

    public void cancelValidation();

    public void setValidation(boolean validation);

}
