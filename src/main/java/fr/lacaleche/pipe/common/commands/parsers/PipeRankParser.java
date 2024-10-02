package fr.lacaleche.pipe.common.commands.parsers;

import fr.lacaleche.core.commands.parsers.RankParser;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeRank;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.parser.ParserDescriptor;

public class PipeRankParser<C> extends RankParser<C, PipeRank> {

    public static <C> @NonNull ParserDescriptor<C, PipeRank> pipeRankParser() {
        return ParserDescriptor.of(new PipeRankParser<>(), PipeRank.class);
    }

}
