package fr.lacaleche.pipe.common.commands.utils;

import fr.lacaleche.pipe.common.i18n.builder.TranslationBuilder;

import java.util.Collection;
import java.util.Optional;

public class EntitySelectorResult<T> {

    private Collection<T> entities;
    private TranslationBuilder error;

    public EntitySelectorResult(Collection<T> entities) {
        this.entities = entities;
    }

    public EntitySelectorResult(TranslationBuilder error) {
        this.error = error;
    }

    public T first() {
        Optional<T> player = this.getEntities().stream().findFirst();
        if (this.hasError() || player.isEmpty()) return null;
        return player.get();
    }

    public Collection<T> getEntities() {
        return entities;
    }

    public TranslationBuilder getError() {
        return error;
    }

    public int size() {
        return entities.size();
    }

    public boolean hasError() {
        return error != null;
    }
}