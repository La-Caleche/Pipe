package fr.lacaleche.pipe.bukkit.modules.chat.renderers;

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractRenderer implements ChatRenderer, PipeViewerUnaware {

    private final PipeViewerUnaware unaware;

    private @MonotonicNonNull Component message;

    public AbstractRenderer(final PipeViewerUnaware unaware) {
        this.unaware = unaware;
    }

    @Override
    public @NotNull Component render(final @NotNull Player source, final @NotNull Component sourceDisplayName, final @NotNull Component message, final @NotNull Audience viewer) {
        return this.render(source, sourceDisplayName, message);
    }

    @Override
    public @NotNull Component render(final @NotNull CommandSender source, final @NotNull Component sourceDisplayName, final @NotNull Component message) {
        if (this.message == null) {
            this.message = this.unaware.render(source, sourceDisplayName, message);
        }
        return this.message;
    }

}
