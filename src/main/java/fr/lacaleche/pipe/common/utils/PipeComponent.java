package fr.lacaleche.pipe.common.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public interface PipeComponent {

    static Component separator() {
        return Component.text(" : ").color(TextColor.fromHexString("#a6a49c"));
    }

}
