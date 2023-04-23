package fr.lacaleche.pipe.bukkit.tabs.features.interfaces;

import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;

public interface NameTagLineChangedListener {

    void onLineChanged(TabPlayer viewer, TabPlayer player, int line);

}
