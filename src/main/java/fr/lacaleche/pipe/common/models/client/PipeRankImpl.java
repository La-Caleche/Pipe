package fr.lacaleche.pipe.common.models.client;

import fr.lacaleche.core.models.clients.ranks.AbstractCoreRank;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeLocale;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeRank;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class PipeRankImpl extends AbstractCoreRank implements PipeRank {

    @Override
    public Component colorize(String text) {
        return Pipe.get().text().deserialize("%s%s".formatted(this.getFormattedColor(), text));
    }

    @Override
    public TextColor colorAsColor() {
        return Pipe.get().text().deserialize(this.getFormattedColor()).color();
    }

    @Override
    public Component getColoredRankName(PipeLocale locale) {
        return this.colorize(this.translatedName(locale));
    }

}
