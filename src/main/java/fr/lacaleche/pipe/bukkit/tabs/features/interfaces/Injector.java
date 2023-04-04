package fr.lacaleche.pipe.bukkit.tabs.features.interfaces;

import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;

public interface Injector {

    void inject(TabPlayer tabPlayer);

    void uninject(TabPlayer tabPlayer);

}
