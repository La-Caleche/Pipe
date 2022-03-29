package fr.lacaleche.pipe;

import org.bukkit.plugin.java.JavaPlugin;

public interface Pipe {

    public JavaPlugin getPlugin();

    public void setPlugin(JavaPlugin plugin);

    public static Pipe get() {
        return PipeImpl.get();
    }

}
