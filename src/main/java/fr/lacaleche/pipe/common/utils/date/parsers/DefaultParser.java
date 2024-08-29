package fr.lacaleche.pipe.common.utils.date.parsers;

import fr.lacaleche.pipe.common.utils.date.interfaces.IParser;

public abstract class DefaultParser implements IParser {

    @Override
    public boolean match(String value) {
        return value.matches("([0-9]+)%s".formatted(getFormat().parser()));
    }
}

