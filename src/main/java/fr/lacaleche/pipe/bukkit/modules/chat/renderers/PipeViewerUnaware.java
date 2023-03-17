package fr.lacaleche.pipe.bukkit.modules.chat.renderers;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface PipeViewerUnaware {

    /**
     * Renders a chat message.
     *
     * @param source the message source
     * @param sourceDisplayName the display name of the source player
     * @param message the chat message
     * @return a rendered chat message
     */
    @ApiStatus.OverrideOnly
    @NotNull
    Component render(@NotNull CommandSender source, @NotNull Component sourceDisplayName, @NotNull Component message);

}
