package fr.lacaleche.pipe.bukkit.modules.chat.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.chat.renderers.PipeRenderer;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.arguments.StringArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import net.kyori.adventure.text.Component;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@MinecraftCommand(label = "chat", aliases = {"c"}, description = "pipe.command.chat.description", arguments = {"message"})

public class ChatCommand {
    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new StringArgument("text").setMultiple(true));
    }

    @CommandExecutor(executors = {CommandExecutor.Executor.SERVER})
    public boolean execute(Command<ConsoleCommandSender> command) {
        Plugin plugin = Pipe.getBukkit().getPlugin();
        String message = command.args().getString("text");

        PipeRenderer renderer = new PipeRenderer(command.sender());
        Component messageComponent = Pipe.getBukkit().text().deserialize(message);
        Component component = renderer.render(command.sender(), Component.text("Console"), messageComponent);

        plugin.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(component));
        command.sender().sendMessage(component);

        return true;
    }
}
