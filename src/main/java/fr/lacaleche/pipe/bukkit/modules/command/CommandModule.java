package fr.lacaleche.pipe.bukkit.modules.command;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.ICentralModuleManager;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.command.commands.*;
import fr.lacaleche.pipe.bukkit.modules.command.listeners.CommandListeners;
import fr.lacaleche.pipe.bukkit.modules.command.listeners.HelpListener;
import fr.lacaleche.pipe.bukkit.modules.command.listeners.TeleportListener;
import fr.lacaleche.pipe.common.packets.CheckPermissionsPacket;
import fr.lacaleche.pipe.common.packets.HelpPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Module to manage Minecraft Commands
 *
 * @author Malo ALLAIN
 * @since 1.0.0
 */
@AModule(target = ModuleTarget.BUKKIT)
public class CommandModule extends BukkitModule {

    private List<Class<?>> commands = new ArrayList<>();

    public CommandModule(IModuleHandler handler) {
        super(handler);
        ICentralModuleManager centralModuleManager = Core.get().getCentralModuleManager();
        if (centralModuleManager.getModules().size() > 0) {
            Logger.err("Currently %d modules is registered", centralModuleManager.getModules().size());
            Pipe.getBukkit().shutdown("Command module must be loaded first. Please disable all modules and restart the server.");
        }
    }

    @Override
    public void onEnable() {
        this.commands = new ArrayList<>();

        this.commands.add(BackCommand.class);
        this.commands.add(CommandBlockCommand.class);
        this.commands.add(FlyCommand.class);
        this.commands.add(GameModeCommand.class);
        this.commands.add(HelpCommand.class);
        this.commands.add(InvisibleCommand.class);
        this.commands.add(NightVisionCommand.class);
        this.commands.add(PipeDebugCommand.class);
        this.commands.add(SpeedCommand.class);
        this.commands.add(TeleportCommand.class);
        this.commands.add(TeleportPositionCommand.class);
    }

    @Override
    public void onDisable() {
        this.commands.clear();
        this.commands = null;
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.getBukkit().getListenerManager();
        bukkitManager.registerBukkitListener(this, new CommandListeners());
        bukkitManager.registerCustomListener(this, new HelpListener());
        bukkitManager.registerBukkitListener(this, new TeleportListener());
    }

    @Override
    public void registerCommands() {
        this.commands.forEach(command -> Pipe.getBukkit().getCommandManager().registerNewCommand(this, command));
    }

    @Override
    public void registerPackets() {
        Core.get().getPacketManager().registerPacket(HelpPacket.class);
        Core.get().getPacketManager().registerPacket(CheckPermissionsPacket.class);
    }

}
