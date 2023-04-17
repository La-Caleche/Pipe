package fr.lacaleche.pipe.bukkit.tabs.features.interfaces;

import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;

public interface PacketWriteListener {

    void writePacket(TabPlayer tabPlayer, Object packet);

}
