package fr.lacaleche.pipe.bukkit.tabs.features;

import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.PacketWriteListener;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageFields;

import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageClass.PACKET_PLAY_OUT_ENTITY;

public class PacketListenerFeature extends AbstractTabFeature implements PacketWriteListener {

    @Override
    public void writePacket(TabPlayer receiver, Object packet) {
        if (this.storage().clazz(PACKET_PLAY_OUT_ENTITY).isInstance(packet)) {
            this.tab().entityMove(receiver, (int) this.storage().get(TabStorageFields.PACKET_PLAY_OUT_ENTITY$ENTITY_ID, packet));
        }
    }
}