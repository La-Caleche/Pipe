package fr.lacaleche.pipe.common.models.client.interfaces;

import fr.lacaleche.core.models.clients.ranks.interfaces.CoreRank;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public interface PipeRank extends CoreRank {

    Component colorize(String text);

    TextColor colorAsColor();

    Component getColoredRankName(PipeLocale locale);

}
