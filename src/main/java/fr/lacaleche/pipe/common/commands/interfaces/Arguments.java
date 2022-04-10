package fr.lacaleche.pipe.common.commands.interfaces;

import fr.lacaleche.pipe.common.commands.argument.interfaces.Argument;

public interface Arguments {

    public Argument get(String key);

    public boolean exist(String key);

    public boolean blank(String key);

    public boolean mandatory(String key);

}
