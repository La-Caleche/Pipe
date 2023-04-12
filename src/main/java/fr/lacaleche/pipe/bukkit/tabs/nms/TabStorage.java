package fr.lacaleche.pipe.bukkit.tabs.nms;

import com.mojang.authlib.GameProfile;
import fr.lacaleche.pipe.bukkit.modules.nms.DefaultStorage;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.utils.ClassFinder;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardTeam;

import java.util.Collection;
import java.util.EnumSet;
import java.util.UUID;

import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageClass.*;
import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageConstructor.*;
import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageMethods.*;
import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageFields.*;

public class TabStorage extends DefaultStorage {

    public TabStorage(TabNMSManager nmsManager) {
        super(nmsManager);
        this.registerStorages();
    }

    private void registerStorages() {
        ClassFinder classFinder = this.getNmsManager().getClassFinder();

        this.registerClass(REMOTE_CHAT_SESSION_DATA, classFinder.networkClass("chat.RemoteChatSession$a"));
        this.registerClass(SCOREBOARD, classFinder.worldClass("scores.Scoreboard"));
        this.registerClass(SCOREBOARD_TEAM, classFinder.worldClass("scores.ScoreboardTeam"));

        this.registerClass(TEAM$TAG_VISIBILITY, classFinder.worldClass("scores.ScoreboardTeamBase$EnumNameTagVisibility"));
        this.registerClass(TEAM$COLLISION_RULE, classFinder.worldClass("scores.ScoreboardTeamBase$EnumTeamPush"));

        this.registerClass(PACKET_CLIENTBOUND_PLAYER_INFO_UPDATE, classFinder.protocolClass("game.ClientboundPlayerInfoUpdatePacket"));
        this.registerClass(PACKET_CLIENTBOUNT_SET_PLAYER_TEAM, classFinder.protocolClass("game.PacketPlayOutScoreboardTeam"));

        this.registerClass(PCB_PLAYER_INFO_DATA, classFinder.protocolClass("game.ClientboundPlayerInfoUpdatePacket$b"));
        this.registerClass(PCB_PLAYER_INFO_ACTION, classFinder.protocolClass("game.ClientboundPlayerInfoUpdatePacket$a"));

        this.registerConstructor(PACKET_CLIENTBOUND_PLAYER_INFO_UPDATE_CONSTRUCTOR, this.getConstructor(PACKET_CLIENTBOUND_PLAYER_INFO_UPDATE, EnumSet.class, Collection.class));

        this.registerConstructor(PCB_PLAYER_INFO_DATA_CONSTRUCTOR, this.getConstructor(PCB_PLAYER_INFO_DATA, UUID.class, GameProfile.class, boolean.class, int.class, EnumGamemode.class, IChatBaseComponent.class, this.clazz(REMOTE_CHAT_SESSION_DATA)));

        this.registerConstructor(SCOREBOARD_CONSTRUCTOR, this.getConstructor(SCOREBOARD));
        this.registerConstructor(SCOREBOARD_TEAM_CONSTRUCTOR, this.getConstructor(SCOREBOARD_TEAM, this.clazz(SCOREBOARD), String.class));

        this.registerMethod(PCB_PLAYER_INFO_DATA$GET_PROFILE, this.getMethod(PCB_PLAYER_INFO_DATA, "b"));
        this.registerMethod(SCOREBOARD_TEAMS$GET_PLAYERS, this.getMethod(SCOREBOARD_TEAM, "g"));
        this.registerMethod(SCOREBOARD_TEAMS$SET_ALLOW_FRIENDLY_FIRE, this.getMethod(SCOREBOARD_TEAM, "a", boolean.class));
        this.registerMethod(SCOREBOARD_TEAMS$SET_CAN_SEE_FRIENDLY_INVISIBILES, this.getMethod(SCOREBOARD_TEAM, "b", boolean.class));
        this.registerMethod(SCOREBOARD_TEAMS$SET_NAME_TAG_VISIBILITY, this.getMethod(SCOREBOARD_TEAM, "a", this.clazz(TEAM$TAG_VISIBILITY)));
        this.registerMethod(SCOREBOARD_TEAMS$SET_COLLISION_RULE, this.getMethod(SCOREBOARD_TEAM, "a", this.clazz(TEAM$COLLISION_RULE)));

        this.registerMethod(PACKET_CLIENTBOUNT_SET_PLAYER_TEAM$CREATE_ADD_OR_MODIFY_PACKET, this.getMethod(PACKET_CLIENTBOUNT_SET_PLAYER_TEAM, "a", this.clazz(SCOREBOARD_TEAM), boolean.class));
        this.registerMethod(PACKET_CLIENTBOUNT_SET_PLAYER_TEAM$CREATE_REMOVE_PACKET, this.getMethod(PACKET_CLIENTBOUNT_SET_PLAYER_TEAM, "a", this.clazz(SCOREBOARD_TEAM)));

        this.registerField(PCB_PLAYER_INFO_UPDATE$ACTIONS, this.getField(PACKET_CLIENTBOUND_PLAYER_INFO_UPDATE, "a"));
        this.registerField(PCB_PLAYER_INFO_UPDATE$PLAYERS, this.getField(PACKET_CLIENTBOUND_PLAYER_INFO_UPDATE, "b"));
        this.registerField(PCB_PLAYER_INFO_DATA$CHAT_SESSION, this.getField(PCB_PLAYER_INFO_DATA, "g"));
        this.registerField(PCB_PLAYER_INFO_DATA$GAME_MODE, this.getField(PCB_PLAYER_INFO_DATA, "e"));
        this.registerField(PCB_PLAYER_INFO_DATA$LATENCY, this.getField(PCB_PLAYER_INFO_DATA, "d"));
        this.registerField(PCB_PLAYER_INFO_DATA$DISPLAY_NAME, this.getField(PCB_PLAYER_INFO_DATA, "f"));
        this.registerField(PCB_PLAYER_INFO_DATA$LISTED, this.getField(PCB_PLAYER_INFO_DATA, "c"));
    }

}
