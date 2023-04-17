package fr.lacaleche.pipe.bukkit.tabs.features.interfaces;

import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;

public interface PacketReadListener {

    void readPacket(TabPlayer tabPlayer, Object packet);

}
