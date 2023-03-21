package fr.lacaleche.pipe.bukkit.modules.warps;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.warps.commands.WarpCommand;
import fr.lacaleche.pipe.bukkit.modules.warps.warp.WarpImpl;

import java.util.List;

@AModule(target = ModuleTarget.BUKKIT)
public class WarpsModule extends BukkitModule {

    public WarpsModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        new ModelFilter<WarpImpl>().list(WarpImpl.class, null, sqlBuilder -> {
            sqlBuilder.where(new Where("host", Core.get().getHost()));
        }, true);
    }

    @Override
    public void registerCommands() {
        Pipe.get().getCommandManager().registerNewCommand(this, WarpCommand.class);
    }
}
