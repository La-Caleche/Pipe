package fr.lacaleche.pipe.bukkit.commands.arguments;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.argument.arguments.DefaultArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BukkitPlayerArgument extends DefaultArgument {

    private boolean allowSelf = false;
    private boolean allowAll = false;
    private boolean allowRandom = false;
    private boolean allowNearest = false;

    public BukkitPlayerArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        completer.addAll(Pipe.getBukkit().<JavaPlugin>getPlugin().getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
        if (allowSelf) completer.addAll(new String[]{"@s", "self"});
        if (allowAll) completer.addAll(new String[]{"@a", "all", "*"});
        if (allowRandom) completer.addAll(new String[]{"@r", "random"});
        if (allowNearest) completer.addAll(new String[]{"@p", "nearest"});
    }

    public BukkitPlayerArgument allowSelf() {
        allowSelf = true;
        return this;
    }

    public BukkitPlayerArgument allowAll() {
        allowAll = true;
        return this;
    }

    public BukkitPlayerArgument allowRandom() {
        allowRandom = true;
        return this;
    }

    public BukkitPlayerArgument allowNearest() {
        allowNearest = true;
        return this;
    }

    public BukkitPlayerArgument allowFull() {
        allowSelf = true;
        allowAll = true;
        allowRandom = true;
        allowNearest = true;
        return this;
    }

    public boolean isAllowSelf() {
        return allowSelf;
    }

    public boolean isAllowAll() {
        return allowAll;
    }

    public boolean isAllowRandom() {
        return allowRandom;
    }

    public boolean isAllowNearest() {
        return allowNearest;
    }

}
