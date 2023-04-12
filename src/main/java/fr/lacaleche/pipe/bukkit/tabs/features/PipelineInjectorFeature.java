package fr.lacaleche.pipe.bukkit.tabs.features;

import com.mojang.authlib.GameProfile;
import fr.lacaleche.core.Core;
import fr.lacaleche.core.utils.Token;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.Injector;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.JoinListener;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.Loadable;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.Unloadable;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.IChatBaseComponent;
import org.jetbrains.annotations.NotNull;

import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageFields.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods.*;

import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageClass.*;
import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageConstructor.*;
import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageFields.*;
import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageMethods.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class PipelineInjectorFeature extends AbstractTabFeature implements Loadable, Unloadable, JoinListener, Injector {

    private final String injectPosition;

    private final Function<TabPlayer, ChannelDuplexHandler> channelFunction = TabChannelDuplexHandler::new;

    public PipelineInjectorFeature(String injectPosition) {
        this.injectPosition = injectPosition;
    }

    @Override
    public void load() {
        for (TabPlayer tabPlayer : this.tab().getTabPlayers()) {
            this.inject(tabPlayer);
        }
    }

    @Override
    public void unload() {
        for (TabPlayer tabPlayer : this.tab().getTabPlayers()) {
            this.uninject(tabPlayer);
        }
    }

    @Override
    public void join(TabPlayer tabPlayer) {
        this.inject(tabPlayer);
    }

    @Override
    public void inject(TabPlayer tabPlayer) {
        final Channel channel = this.getChannel(tabPlayer);
        if (channel == null) return;
        if (!channel.pipeline().names().contains(injectPosition)) {
            //fake player or bug
            return;
        }
        this.uninject(tabPlayer);
        try {
            channel.pipeline().addBefore(injectPosition, Core.get().getAppName(), this.getChannelFunction().apply(tabPlayer));
        } catch (NoSuchElementException | IllegalArgumentException ignored) {
        }
    }

    @Override
    public void uninject(TabPlayer tabPlayer) {
        final Channel channel = this.getChannel(tabPlayer);
        if (channel == null) return;
        try {
            if (channel.pipeline().names().contains(Core.get().getAppName()))
                channel.pipeline().remove(Core.get().getAppName());
        } catch (NoSuchElementException ignored) {}
    }

    private Channel getChannel(TabPlayer tabPlayer) {
        Object connection = this.tab().getNmsManager().getPlayerConnection(tabPlayer.getPlayer());
        if (connection == null) return null;

        Object networkManager = this.storage().get(PLAYER_CONNECTION$NETWORK_MANAGER, connection);
        if (networkManager == null) return null;

        return this.storage().get(NETWORK_MANAGER$CHANNEL, networkManager);
    }

    public boolean isTeam(Object packet) {
        return false;
    }

    public boolean isPlayerInfo(Object packet) {
        return this.storage().clazz(PACKET_CLIENTBOUND_PLAYER_INFO_UPDATE).isInstance(packet);
    }

    public void onPlayerInfo(TabPlayer receiver, Object packet) {
        List<String> actions = ((EnumSet<?>) this.storage().get(PCB_PLAYER_INFO_UPDATE$ACTIONS, packet)).stream().map(Enum::name).toList();
        List<Object> updatedList = new ArrayList<>();

        for (Object playerDataEntry : (List<?>) this.storage().get(PCB_PLAYER_INFO_UPDATE$PLAYERS, packet)) {
            GameProfile profile = (GameProfile) this.storage().invoke(PCB_PLAYER_INFO_DATA$GET_PROFILE, playerDataEntry);
            int gameMode = 0;
            int latency = 0;
            Component displayName = null;
            boolean listed = this.storage().get(PCB_PLAYER_INFO_DATA$LISTED, playerDataEntry);
            Object chatSession = this.storage().get(PCB_PLAYER_INFO_DATA$CHAT_SESSION, playerDataEntry);

            if (actions.contains("UPDATE_GAME_MODE") || actions.contains("ADD_PLAYER")) {
                gameMode = this.tab().gameMode2Int(this.storage().get(PCB_PLAYER_INFO_DATA$GAME_MODE, playerDataEntry));
                gameMode = this.tab().onGameModeChange(receiver, profile.getId(), gameMode);
            }

            if (actions.contains("UPDATE_LATENCY") || actions.contains("ADD_PLAYER")) {
                latency = this.storage().get(PCB_PLAYER_INFO_DATA$LATENCY, playerDataEntry);
                latency = this.tab().onLatencyChange(receiver, profile.getId(), latency);
            }

            if (actions.contains("UPDATE_DISPLAY_NAME") || actions.contains("ADD_PLAYER")) {
                IChatBaseComponent displayNameComponent = this.storage().get(PCB_PLAYER_INFO_DATA$DISPLAY_NAME, playerDataEntry);
                displayName = displayNameComponent == null ? null : this.storage().invoke(ADVENTURE_COMPONENT$GET_COMPONENT, this.storage().cast(ADVENTURE_COMPONENT, displayNameComponent));
                displayName = this.tab().onDisplayNameChange(receiver, profile.getId(), displayName);
            }

            updatedList.add(this.storage().construct(PCB_PLAYER_INFO_DATA_CONSTRUCTOR,
                    profile.getId(),
                    profile,
                    listed,
                    latency,
                    this.tab().int2GameMode(gameMode),
                    this.storage().construct(ADVENTURE_COMPONENT_CONSTRUCTOR, displayName),
                    chatSession
            ));
        }

        this.storage().set(PCB_PLAYER_INFO_UPDATE$PLAYERS, packet, updatedList);
    }

    public void modifyPlayers(Object teamPacket) {
    }

    private Function<TabPlayer, ChannelDuplexHandler> getChannelFunction() {
        return channelFunction;
    }

    public class TabChannelDuplexHandler extends ChannelDuplexHandler {

        protected final TabPlayer player;

        public TabChannelDuplexHandler(TabPlayer player) {
            this.player = player;
        }

        @Override
        public void write(ChannelHandlerContext context, Object packet, ChannelPromise channelPromise) {
            try {
                if (isPlayerInfo(packet)) onPlayerInfo(player, packet);
                // if (isTeam(packet))  modifyPlayers(packet);
                // getTab().onPacketSend(player, packet);
            } catch (Throwable exception) {
                SentryAPIImpl.getInstance().captureException(exception);
            }

            try {
                super.write(context, packet, channelPromise);
            } catch (Throwable exception) {
                Logger.catchThrowable("Failed to forward packet %s to %s", exception, packet.getClass().getSimpleName(), player.getName());
            }
        }
    }

}