package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.arguments.BooleanArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

@MinecraftCommand(label = "commandblocks", description = "pipe.command.commandblocks.description", aliases = {"cb"}, arguments = {"enabled"})
public class CommandBlockCommand {

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BooleanArgument("enabled"));
    }

    @CommandExecutor
    public boolean execute(Command<CommandSender> command) {
        Locale locale = Pipe.getBukkit().getDefaultLocale();

        if (command.sender() instanceof Player player) {
            Client client = Pipe.getBukkit().getClient(player.getUniqueId());
            locale = client.getLocale();
        }

        Field field = this.getCommandBlockEnabledField();

        if (field == null) {
            command.sender().sendMessage(locale.t("pipe.command.commandblocks.error").ct());
            return true;
        }

        boolean enabled = command.args().getBoolean("enabled");
        this.setCommandBlockEnabled(enabled);

        if (this.getCommandBlockEnabled() != enabled) {
            command.sender().sendMessage(locale.t("pipe.command.commandblocks.error").ct());
            return true;
        }

        command.sender().sendMessage(locale.ct("pipe.command.commandblocks.enabled", "pipe.command.commandblocks.disabled", enabled).ct());

        return true;
    }

    private Field getCommandBlockEnabledField() {
        try {
            DedicatedServerProperties properties = this.getServerProperties();

            Field field = properties.getClass().getDeclaredField("y");
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException ignored) {
            Logger.warn("Unable to find command block enabled field");
            return null;
        }
    }

    private DedicatedServerProperties getServerProperties() {
        Plugin plugin = Pipe.getBukkit().getPlugin();
        DedicatedServer server = ((CraftServer) plugin.getServer()).getServer();
        return server.a();
    }

    private void setCommandBlockEnabled(boolean enabled) {
        Field field = this.getCommandBlockEnabledField();

        if (field == null) {
            return;
        }

        try {
            field.set(this.getServerProperties(), enabled);
        } catch (IllegalAccessException ignored) {
            Logger.warn("Unable to set command block enabled field");
        }
    }

    private boolean getCommandBlockEnabled() {
        Field field = this.getCommandBlockEnabledField();

        if (field == null) {
            return false;
        }

        try {
            return field.getBoolean(this.getServerProperties());
        } catch (IllegalAccessException ignored) {
            Logger.warn("Unable to get command block enabled field");
            return false;
        }
    }

}
