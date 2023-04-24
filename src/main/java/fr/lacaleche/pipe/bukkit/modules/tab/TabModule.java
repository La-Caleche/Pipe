package fr.lacaleche.pipe.bukkit.modules.tab;

import com.google.common.collect.ImmutableList;
import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.BukkitPipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.tab.commands.TabCommand;
import fr.lacaleche.pipe.bukkit.modules.tab.listeners.TabPlayerListener;
import fr.lacaleche.pipe.bukkit.tabs.nametag.models.PersistentNametagImpl;
import fr.lacaleche.pipe.bukkit.tabs.features.NameTagFeature;
import fr.lacaleche.pipe.bukkit.tabs.features.PipelineInjectorFeature;
import fr.lacaleche.pipe.bukkit.tabs.playerlist.features.PlayerListFeature;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.playerlist.features.SortingFeature;
import fr.lacaleche.pipe.common.clients.Client;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.List;

@AModule(target = ModuleTarget.BUKKIT)
public class TabModule extends BukkitModule {

    public TabModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        BukkitPipe pipe = Pipe.getBukkit();

        pipe.addJoinCallback(this, (playerJoinEvent, player, client) -> {
            TabPlayer tabPlayer = Pipe.getBukkit().getTabManager().createPlayer(player);
            Pipe.getBukkit().getTabManager().loadPlayer(tabPlayer);
        });

        pipe.addQuitCallbacks(this, (playerQuitEvent, player, client) -> {
            if (Core.get().isDisabling()) return;
            TabPlayer tabPlayer = Pipe.getBukkit().getTabManager().getTabPlayer(player.getUniqueId());
            Pipe.getBukkit().getTabManager().unloadPlayer(tabPlayer);
        });

        pipe.getTabManager().registerFeature("PlayerList", new PlayerListFeature());
        pipe.getTabManager().registerFeature("Sorting", new SortingFeature());
        pipe.getTabManager().registerFeature("PipelineInjector", new PipelineInjectorFeature("packet_handler"));
        pipe.getTabManager().registerFeature("NameTag", new NameTagFeature());
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager listenerManager = Pipe.getBukkit().getListenerManager();
        listenerManager.registerBukkitListener(this, new TabPlayerListener());
    }

    @Override
    public void registerCommands() {
        Pipe.getBukkit().getCommandManager().registerNewCommand(this, TabCommand.class);
    }

    @Override
    public void onDisable() {
        BukkitPipe pipe = Pipe.getBukkit();

        pipe.removeJoinCallbacks(this);
        pipe.removeQuitCallbacks(this);

        ImmutableList.copyOf(pipe.getTabManager().getFeatures().values())
                .forEach(pipe.getTabManager()::unregisterFeature);

        ImmutableList.copyOf(pipe.getTabManager().getTabPlayers()).forEach(tabPlayer -> {
            pipe.getTabManager().unloadPlayer(tabPlayer);
        });
    }

    @Override
    public void onReload() {
        BukkitPipe pipe = Pipe.getBukkit();
        Plugin plugin = pipe.getPlugin();

        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        if (players.size() == 0) {
            super.onReload();
            return;
        }

        for (Player player : players) {
            Client client = Pipe.getBukkit().getClient(player.getUniqueId());
            pipe.getQuitCallbacks().get(this).forEach(callback -> callback.accept(null, player, client));
        }

        super.onReload();

        for (Player player : players) {
            Client client = Pipe.getBukkit().getClient(player.getUniqueId());
            pipe.getJoinCallbacks().get(this).forEach(callback -> callback.accept(null, player, client));
        }
    }

    @Override
    public void onEnableFinish() {
        Pipe.getBukkit().getTabManager().loadFeatures();
    }
}
