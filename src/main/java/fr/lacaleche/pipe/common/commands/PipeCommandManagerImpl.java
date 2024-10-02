package fr.lacaleche.pipe.common.commands;

import fr.lacaleche.core.commands.CoreCommandManagerImpl;
import fr.lacaleche.pipe.common.commands.parsers.PipeLocaleParser;
import fr.lacaleche.pipe.common.commands.parsers.PipeRankParser;
import org.incendo.cloud.CommandManager;

public abstract class PipeCommandManagerImpl<C> extends CoreCommandManagerImpl<C> {

    public PipeCommandManagerImpl(CommandManager<C> commandManager) {
        this.setCloudCommandManager(commandManager);
        this.getCloudCommandManager().parserRegistry()
                .registerParser(PipeLocaleParser.pipeLocaleParser())
                .registerParser(PipeRankParser.pipeRankParser());
    }

}
