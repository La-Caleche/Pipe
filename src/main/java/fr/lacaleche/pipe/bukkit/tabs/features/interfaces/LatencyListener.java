package fr.lacaleche.pipe.bukkit.tabs.features.interfaces;

import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;

import java.util.UUID;

public interface LatencyListener {

    int onLatencyChange(TabPlayer tabPlayer, UUID uuid, int newLatency);

}
