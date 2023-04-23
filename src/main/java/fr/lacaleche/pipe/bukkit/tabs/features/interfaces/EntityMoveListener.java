package fr.lacaleche.pipe.bukkit.tabs.features.interfaces;

import fr.lacaleche.pipe.bukkit.tabs.features.AbstractTabFeature;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.nms.TabStorage;
import fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageFields;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageClass.PACKET_PLAY_OUT_ENTITY;

public interface EntityMoveListener {

    void onEntityMove(TabPlayer viewer, TabPlayer player, boolean force);

}
