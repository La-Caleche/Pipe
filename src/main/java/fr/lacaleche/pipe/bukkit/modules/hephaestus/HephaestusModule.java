package fr.lacaleche.pipe.bukkit.modules.hephaestus;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.ModelRegistry;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.commands.HephaestusCommand;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.interfaces.HephaestusManager;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.track.ModelViewPersistenceHandlerImpl;
import org.jetbrains.annotations.NotNull;
import team.unnamed.hephaestus.Model;
import team.unnamed.hephaestus.bukkit.BukkitModelEngine;
import team.unnamed.hephaestus.bukkit.v1_20_R1.BukkitModelEngine_v1_20_R1;
import team.unnamed.hephaestus.reader.blockbench.BBModelReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@AModule(target = ModuleTarget.BUKKIT)
public class HephaestusModule extends BukkitModule {

    private HephaestusManager manager;

    public HephaestusModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        this.manager = new HephaestusManagerImpl(this);
        this.manager.start();
    }

    @Override
    public void onDisable() {
        this.manager.stop();
        this.manager = null;
    }

    @Override
    public void registerCommands() {
        Pipe.getBukkit().getCommandManager().registerNewCommand(this, HephaestusCommand.class);
    }

    public HephaestusManager getManager() {
        return manager;
    }
}
