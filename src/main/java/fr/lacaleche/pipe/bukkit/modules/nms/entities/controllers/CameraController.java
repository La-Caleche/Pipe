package fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSModule;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageFields;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import fr.lacaleche.pipe.common.tasks.interfaces.Task;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInFlying;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutPosition;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CameraController extends ZombieController {

    private Map<String, DuplexHandler> handlers;

    public CameraController(NMSManager nmsManager, Location location) {
        super(nmsManager, location);
        this.handlers = new HashMap<>();
    }

    @Override
    public void spawn() {
        super.spawn();
        this.setNoGravity(true);
    }

    public void startCamera(Player player) {
        Pipe.get().getTaskManager().newTask(new TaskBuilder().callback(task -> this.setLocation(this.getLocation().clone())).startAfter(2)); // Force update location because of no gravity

        this.addSpectator(player);
        this.addAsPassenger(player);
        this.inject(player, "CameraPacketInjector", PacketPlayInFlying.PacketPlayInLook.class, CameraChannelDuplexHandler::new);
        this.inject(player, "InteractPacketInjector", PacketPlayInUseEntity.class, InteractChannelDuplexHandler::new);
    }

    public void stopCamera(Player player) {
        this.removeSpectator(player);
        this.removePassengers();
        this.handlers.values().forEach(DuplexHandler::close);
        this.handlers.clear();
        this.uninject(player, "CameraPacketInjector");
        this.uninject(player, "InteractPacketInjector");
    }

    private void inject(Player player, String name, Class clazz, BiFunction<Player, Class, DuplexHandler> newFunction) {
        final Channel channel = getChannel(player);
        if (channel == null || channel.pipeline().get(name) != null) return;
        this.uninject(player, name);

        try {
            DuplexHandler handler = newFunction.apply(player, clazz);
            if (handler == null) return;
            channel.pipeline().addAfter("decoder", name, handler);
            this.handlers.put(name, handler);
        } catch (NoSuchElementException | IllegalArgumentException ignored) {
        }
    }

    private void uninject(Player player, String name) {
        final Channel channel = getChannel(player);
        if (channel == null) return;
        try {
            if (channel.pipeline().get(name) != null)
                channel.pipeline().remove(name);
        } catch (NoSuchElementException ignored) {}
    }

    private Channel getChannel(Player player) {
        NMSManager manager = Core.getModule(NMSModule.class).getNmsManager();
        Object connection = manager.getPlayerConnection(player);
        if (connection == null) return null;

        Object networkManager = manager.getStorage().get(StorageFields.PLAYER_CONNECTION$NETWORK_MANAGER, connection);
        if (networkManager == null) return null;

        return manager.getStorage().get(StorageFields.NETWORK_MANAGER$CHANNEL, networkManager);
    }

    private class CameraChannelDuplexHandler<I> extends MessageToMessageDecoder<I> implements DuplexHandler {

        protected final Player player;
        protected final BatController bat;

        public CameraChannelDuplexHandler(Player player, Class<? extends I> clazz) {
            super(clazz);
            this.player = player;
            this.bat = Core.getModule(NMSModule.class).getNmsManager().createEntity(BatController.class, player.getLocation().clone().set(0, 2, 0));

            this.bat.spawn();
            this.bat.show(player);
        }

        @Override
        public void close() {
            this.bat.hide(player);
            this.bat.remove();
        }

        @Override
        protected void decode(ChannelHandlerContext ctx, I msg, List<Object> out) throws Exception {
            PacketPlayInFlying.PacketPlayInLook packet = (PacketPlayInFlying.PacketPlayInLook) msg;
            Location batLocation = bat.getLocation();

            Location newLocation = batLocation.clone();
            newLocation.set(packet.d / -8, 2, packet.e / -8);
            bat.setLocation(newLocation);
        }
    }

    private class InteractChannelDuplexHandler<I> extends MessageToMessageDecoder<I> implements DuplexHandler {

        protected final Player player;

        public InteractChannelDuplexHandler(Player player, Class<? extends I> clazz) {
            super(clazz);
            this.player = player;
        }

        @Override
        public void close() {
        }

        @Override
        protected void decode(ChannelHandlerContext ctx, I msg, List<Object> out) throws Exception {
            // Do nothing for the moment, but avoid crash because if "Can't interact with self" error.
        }
    }

    private interface DuplexHandler extends ChannelHandler {

        void close();

    }

}
