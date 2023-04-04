package fr.lacaleche.pipe.bukkit.tabs.features.interfaces;

import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface DisplayNameListener {

    Component onDisplayNameChange(TabPlayer tabPlayer, UUID uuid, Component newDisplayName);

}
