package fr.lacaleche.pipe.common.commands;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.commands.AbstractCommandContext;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeClient;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeLocale;

import java.util.Optional;

public class PipeCommandContext<S, C extends PipeClient<? extends S>> extends AbstractCommandContext<S, C, PipeLocale> {

    public PipeCommandContext(S sender) {
        super(sender, (PipeLocale) Core.get().getLocale(sender), (C) Core.get().getClient(sender));
    }

    @Override
    public PipeLocale locale() {
        return this.locale;
    }

    @Override
    public C client() {
        return this.client;
    }

    @Override
    public <T> Optional<C> searchClient(T target) {
        return Optional.ofNullable((C) Core.get().getClient(target));
    }

    @Override
    public S sender() {
        return this.sender;
    }

    @Override
    public C clientOrReject(C recipient) {
        if (this.client() == null && recipient == null) {
            this.sendMessage(locale().t("global.only_for_players").buildComponent());
            return null;
        }
        return recipient == null ? this.client() : recipient;
    }
}
