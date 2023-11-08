package fr.lacaleche.pipe.bukkit.modules.command;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.ICentralModuleManager;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.BukkitPipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.command.commands.*;
import fr.lacaleche.pipe.bukkit.modules.command.listeners.CommandListeners;
import fr.lacaleche.pipe.bukkit.modules.command.listeners.HelpListener;
import fr.lacaleche.pipe.bukkit.modules.command.listeners.TeleportListener;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSModule;
import fr.lacaleche.pipe.bukkit.tabs.features.PipelineInjectorFeature;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.common.packets.CheckPermissionsPacket;
import fr.lacaleche.pipe.common.packets.HelpPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.joor.Reflect;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageFields.NETWORK_MANAGER$CHANNEL;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageFields.PLAYER_CONNECTION$NETWORK_MANAGER;

/**
 * Module to manage Minecraft Commands
 *
 * @author Malo ALLAIN
 * @since 1.0.0
 */
@AModule(target = ModuleTarget.BUKKIT)
public class CommandModule extends BukkitModule {

    private List<Class<?>> commands = new ArrayList<>();
    private Map<Player, PermissibleBase> permissibleBaseMap;

    public CommandModule(IModuleHandler handler) {
        super(handler);
        ICentralModuleManager centralModuleManager = Core.get().getCentralModuleManager();
        if (centralModuleManager.getModules().size() > 0) {
            Logger.err("Currently %d modules is registered", centralModuleManager.getModules().size());
            Pipe.getBukkit().shutdown("Command module must be loaded first. Please disable all modules and restart the server.");
        }

        this.permissibleBaseMap = new HashMap<>();
    }

    @Override
    public void onEnable() {
        BukkitPipe pipe = Pipe.getBukkit();
        NMSModule nmsModule = Core.getModule(NMSModule.class);

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

        pipe.addJoinCallback(this, (playerJoinEvent, player, client) -> {
            CraftPlayer craftPlayer = (CraftPlayer) player;
            Reflect reflect = Reflect.on(craftPlayer);
            this.permissibleBaseMap.put(player, reflect.get("perm"));
            reflect.set("perm", new CustomPermissibleBase(craftPlayer));
        });

        pipe.addQuitCallbacks(this, (playerQuitEvent, player, client) -> {
            CraftPlayer craftPlayer = (CraftPlayer) player;
            Reflect reflect = Reflect.on(craftPlayer);
            reflect.set("perm", this.permissibleBaseMap.get(player));
            this.permissibleBaseMap.remove(player);
        });
    }

    @Override
    public void onDisable() {
        this.commands.clear();
        this.commands = null;

        this.permissibleBaseMap.clear();
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
