package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.bukkit.utils.PipeCommandUtils;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.*;
import fr.lacaleche.pipe.common.commands.argument.arguments.CustomArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@MinecraftCommand(label = "gamemode", aliases = {"gm"}, arguments = {"mode", "player"}, description = "pipe.command.gamemode.description")
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
            sender.sendMessage(locale.t("pipe.command.gamemode.invalid_mode").ct());
            return true;
        }

        PipeCommandUtils.PlayerResult result = PipeCommandUtils.getPlayerFromArgsOrSender(sender, arguments, "player");
        if (result.hasError()) {
            sender.sendMessage(result.getError().ct());
            return true;
        }

        Player target = result.getPlayer();

        target.setGameMode(gameMode.getBukkitGameMode());
        sender.sendMessage(locale.t("pipe.command.gamemode.success").arg("mode", requiredGameMode).arg("player", target.getName()).ct());

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

    @CommandChild(label = "get", arguments = {"player"}, description = "pipe.command.gamemode.get.description")
    public static class Get {

        @CommandExecutor
        public boolean execute(CommandSender sender, Arguments arguments) {
            Locale locale = Pipe.get().getDefaultLocale();

            if (sender instanceof Player player) {
                Client client = Pipe.get().getClient(player.getUniqueId());
                locale = client.getLocale();
            }

            PipeCommandUtils.PlayerResult result = PipeCommandUtils.getPlayerFromArgsOrSender(sender, arguments, "player");
            if (result.hasError()) {
                sender.sendMessage(result.getError().ct());
                return true;
            }

            Player target = result.getPlayer();

            sender.sendMessage(locale.t("pipe.command.gamemode.player_mode").arg("mode", target.getGameMode().name()).arg("player", target.getName()).ct());

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
