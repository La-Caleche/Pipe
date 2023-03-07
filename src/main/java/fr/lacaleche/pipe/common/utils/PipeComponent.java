package fr.lacaleche.pipe.common.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

public interface PipeComponent {

    static Component separator() {
        return Component.text(" : ").color(TextColor.fromHexString("#a6a49c"));
    }

    static Component deserialize(String serialized) {
        return MiniMessage.miniMessage().deserialize(serialized);
    }

    static TextColor deserializeColor(String serialized) {
        return MiniMessage.miniMessage().deserialize(serialized).color();
    }

}
