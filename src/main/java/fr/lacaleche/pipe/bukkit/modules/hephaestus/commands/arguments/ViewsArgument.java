package fr.lacaleche.pipe.bukkit.modules.hephaestus.commands.arguments;

import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.HephaestusModule;
import fr.lacaleche.pipe.common.commands.argument.arguments.DefaultArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class ViewsArgument extends DefaultArgument {

    public ViewsArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        HephaestusModule module = Core.getModule(HephaestusModule.class);
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                final var view = module.getManager().getModelEntityById(entity.getUniqueId().toString());
                if (view != null) {
                    completer.add(entity.getUniqueId().toString());
                }
            }
        }
    }

}
