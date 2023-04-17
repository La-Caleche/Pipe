package fr.lacaleche.pipe.bukkit.modules.tab;

import com.google.common.collect.ImmutableList;
import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.tabs.features.NameTagFeature;
import fr.lacaleche.pipe.bukkit.tabs.features.PacketListenerFeature;
import fr.lacaleche.pipe.bukkit.tabs.features.PipelineInjectorFeature;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.Refreshable;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.TabFeature;
import fr.lacaleche.pipe.bukkit.tabs.nametag.interfaces.PlayerNameTag;
import fr.lacaleche.pipe.bukkit.tabs.playerlist.features.PlayerListFeature;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.playerlist.features.SortingFeature;
import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import net.kyori.adventure.text.Component;

@AModule(target = ModuleTarget.BUKKIT)
public class TabModule extends BukkitModule {

    public TabModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        Pipe pipe = Pipe.get();

        pipe.addJoinCallback(this, (playerJoinEvent, player, client) -> {
            TabPlayer tabPlayer = Pipe.get().getTabManager().createPlayer(player);

            PlayerNameTag nameTag = tabPlayer.getNameTag();
            for (int i = 0; i < 3; i++) {
                nameTag.addLine(Component.text("Line " + i), i);
            }

            Pipe.get().getTabManager().loadPlayer(tabPlayer);
        });

        pipe.addQuitCallbacks(this, (playerQuitEvent, player, client) -> {
            if (Core.get().isDisabling()) return;
            TabPlayer tabPlayer = Pipe.get().getTabManager().getTabPlayer(player.getUniqueId());
            Pipe.get().getTabManager().unloadPlayer(tabPlayer);
        });

        pipe.getTabManager().registerFeature("PlayerList", new PlayerListFeature());
        pipe.getTabManager().registerFeature("Sorting", new SortingFeature());
        pipe.getTabManager().registerFeature("PipelineInjector", new PipelineInjectorFeature("packet_handler"));
        pipe.getTabManager().registerFeature("PacketListener", new PacketListenerFeature());
        pipe.getTabManager().registerFeature("NameTag", new NameTagFeature());

    }

    @Override
    public void onDisable() {
        Pipe pipe = Pipe.get();

        pipe.removeJoinCallbacks(this);
        pipe.removeQuitCallbacks(this);

        ImmutableList.copyOf(pipe.getTabManager().getFeatures().values())
                .forEach(pipe.getTabManager()::unregisterFeature);

        ImmutableList.copyOf(pipe.getTabManager().getTabPlayers()).forEach(tabPlayer -> {
            pipe.getTabManager().unloadPlayer(tabPlayer);
        });
    }

    @Override
    public void onEnableFinish() {
        Pipe.get().getTabManager().loadFeatures();
    }
}
