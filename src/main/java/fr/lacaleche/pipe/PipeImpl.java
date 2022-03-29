package fr.lacaleche.pipe;

import org.bukkit.plugin.java.JavaPlugin;

public class PipeImpl implements Pipe {

    public static Pipe instance;
    private JavaPlugin plugin;

    public static Pipe get() {
        return instance;
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void setPlugin(JavaPlugin plugin) {
        if (this.plugin != null) {
            throw new RuntimeException("Plugin is already set");
        }
        this.plugin = plugin;
    }

    public static Pipe init() {
        if (instance != null) {
            throw new RuntimeException("Pipe is already initialized");
        }
        instance = new PipeImpl();
        return instance;
    }

}
