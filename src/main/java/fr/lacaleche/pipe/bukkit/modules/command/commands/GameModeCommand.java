package fr.lacaleche.pipe.bukkit.modules.command.commands;

import dev.jorel.commandapi.arguments.PlayerArgument;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.*;
import fr.lacaleche.pipe.common.commands.argument.arguments.CustomArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@MinecraftCommand(label = "gamemode", aliases = {"gm"}, arguments = {"mode", "player"}, description = "Change your game mode")
public class GameModeCommand {

    @CommandExecutor
    public boolean executor(CommandSender sender, Arguments arguments) {
        Locale locale = Pipe.get().getDefaultLocale();

        if (sender instanceof Player player) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            locale = client.getLocale();
        }

        String requiredGameMode = arguments.getString("mode");

        GameMode gameMode = GameMode.fromAlias(requiredGameMode);

        if (gameMode == null) {
            sender.sendMessage(locale.t("command.gamemode.invalid_mode").ct());
            return true;
        }

        if (arguments.blank("player")) {
            if (sender instanceof Player player) {
                player.setGameMode(gameMode.getBukkitGameMode());
                sender.sendMessage(locale.t("command.gamemode.success").arg("mode", requiredGameMode).ct());
            } else {
                sender.sendMessage(locale.t("command.gamemode.only_for_players").ct());
            }
        } else {
            Player target = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer(arguments.getString("player"));
            if (target == null) {
                sender.sendMessage(locale.t("command.gamemode.player_not_found").arg("player", arguments.getString("player")).ct());
                return true;
            }
            target.setGameMode(gameMode.getBukkitGameMode());
            sender.sendMessage(locale.t("command.gamemode.success").arg("mode", requiredGameMode).arg("player", target.getName()).ct());
        }

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new CustomArgument("mode"));
        manager.addArgument(new BukkitPlayerArgument("player").optional());
    }

    @TabCompleter
    public void completer(Completer completer) {
        if (completer.index() == 1) {
            Arrays.stream(GameMode.values()).map(GameMode::getAliases).forEach(completer::addAll);
        }
    }

    @CommandChild(label = "get", arguments = {"player"}, description = "Get gamemode of a player")
    public static class Get {

        @CommandExecutor
        public boolean execute(CommandSender sender, Arguments arguments) {
            Locale locale = Pipe.get().getDefaultLocale();

            if (sender instanceof Player player) {
                Client client = Pipe.get().getClient(player.getUniqueId());
                locale = client.getLocale();
            }

            Player target = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer(arguments.getString("player"));

            if (target == null) {
                sender.sendMessage(locale.t("command.gamemode.player_not_found").arg("player", arguments.getString("player")).ct());
                return true;
            }

            sender.sendMessage(locale.t("command.gamemode.player").arg("mode", target.getGameMode().name()).arg("player", target.getName()).ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player"));
        }

    }

    private enum GameMode {
        SURVIVAL(org.bukkit.GameMode.SURVIVAL, "survival", "s", "0"), CREATIVE(org.bukkit.GameMode.CREATIVE, "creative", "c", "1"),
        ADVENTURE(org.bukkit.GameMode.ADVENTURE, "adventure", "a", "2"), SPECTATOR(org.bukkit.GameMode.SPECTATOR, "spectator", "sp", "spec", "3");

        private final org.bukkit.GameMode bukkitGameMode;
        private final String[] aliases;

        GameMode(org.bukkit.GameMode bukkitGameMode, String... aliases) {
            this.bukkitGameMode = bukkitGameMode;
            this.aliases = aliases;
        }

        public static GameMode fromAlias(String alias) {
            for (GameMode gameMode : values()) {
                for (String gameModeAlias : gameMode.aliases) {
                    if (gameModeAlias.equalsIgnoreCase(alias)) {
                        return gameMode;
                    }
                }
            }
            return null;
        }

        public String[] getAliases() {
            return aliases;
        }

        public org.bukkit.GameMode getBukkitGameMode() {
            return bukkitGameMode;
        }

    }

}
