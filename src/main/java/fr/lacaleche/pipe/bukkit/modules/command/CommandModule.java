package fr.lacaleche.pipe.bukkit.modules.command;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.command.commands.*;
import fr.lacaleche.pipe.bukkit.modules.command.listeners.CommandListeners;
import fr.lacaleche.pipe.common.commands.listeners.HelpPacketListener;
import fr.lacaleche.pipe.common.commands.listeners.RegisterNetworkCommandPacketListener;
import fr.lacaleche.pipe.common.packets.FetchNetworkCommandsPacket;
import fr.lacaleche.pipe.common.packets.HelpPacket;
import fr.lacaleche.pipe.common.packets.RegisterNetworkCommandPacket;

/**
 * Module to manage Minecraft Commands
 *
 * @author Malo ALLAIN
 * @since 1.0.0
 */
public class CommandModule extends Module {

    public CommandModule(IModuleHandler handler) {
        super(handler);
        if (handler.getModules().size() > 0) {
            Logger.err("Currently %d modules is registered".formatted(handler.getModules().size()));
            Pipe.get().shutdown("Please, unregister all modules before registering this one !");
        }
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerBukkitListener(this, new CommandListeners());
        bukkitManager.registerCustomListener(this, new HelpPacketListener());
        bukkitManager.registerCustomListener(this, new RegisterNetworkCommandPacketListener());
    }

    @Override
    public void registerCommands() {
        FetchNetworkCommandsPacket packet = new FetchNetworkCommandsPacket();
        CalecheCore.get().getPacketManager().publish(packet);

        Pipe.get().getCommandManager().registerNewCommand(this, FlyCommand.class);
        Pipe.get().getCommandManager().registerNewCommand(this, GameModeCommand.class);
        Pipe.get().getCommandManager().registerNewCommand(this, HealCommand.class);
        Pipe.get().getCommandManager().registerNewCommand(this, HelpCommand.class);
        Pipe.get().getCommandManager().registerNewCommand(this, InvisibleCommand.class);
        Pipe.get().getCommandManager().registerNewCommand(this, NightVisionCommand.class);
        Pipe.get().getCommandManager().registerNewCommand(this, SpeedCommand.class);
        Pipe.get().getCommandManager().registerNewCommand(this, TeleportCommand.class);
        Pipe.get().getCommandManager().registerNewCommand(this, TeleportPositionCommand.class);
    }

    @Override
    public void registerPackets() {
        CalecheCore.get().getPacketManager().registerPacket(HelpPacket.class);
        CalecheCore.get().getPacketManager().registerPacket(RegisterNetworkCommandPacket.class);
        CalecheCore.get().getPacketManager().registerPacket(FetchNetworkCommandsPacket.class);
    }
}
