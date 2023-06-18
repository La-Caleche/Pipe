package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.bukkit.modules.command.utils.BukkitEntitySelector;
import fr.lacaleche.pipe.common.commands.annotations.*;
import fr.lacaleche.pipe.common.commands.argument.arguments.CustomArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import fr.lacaleche.pipe.common.commands.utils.EntitySelectorResult;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

@MinecraftCommand(label = "gamemode", aliases = {"gm"}, arguments = {"mode", "player"}, description = "pipe.command.gamemode.description")
public class GameModeCommand {

    @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.gamemode")
    public boolean executor(Command<CommandSender> command) {
        String requiredGameMode = command.args().getString("mode");

        GameMode gameMode = GameMode.fromAlias(requiredGameMode);

        if (gameMode == null) {
            command.sender().sendMessage(command.locale().t("pipe.command.gamemode.invalid_mode").arg("mode", requiredGameMode).from("GameMode").ct());
            return true;
        }

        EntitySelectorResult<Player> result = BukkitEntitySelector.parsePlayers(command, "player");
        if (result.hasError()) {
            command.sender().sendMessage(result.getError().from("GameMode").ct());
            return true;
        }

        Collection<Player> targets = result.getEntities();
        targets.forEach(target -> target.setGameMode(gameMode.getBukkitGameMode()));

        command.sender().sendMessage(command.locale().ct("pipe.command.gamemode.success.one", "pipe.command.gamemode.success.all", targets.size() == 1).arg("mode", requiredGameMode).ph("player", result.first()).from("GameMode").ct());

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new CustomArgument("mode"));
        manager.addArgument(new BukkitPlayerArgument("player").allowFull().optional());
    }

    @TabCompleter
    public void completer(Completer completer) {
        if (completer.index() == 1) {
            Arrays.stream(GameMode.values()).map(GameMode::getAliases).forEach(completer::addAll);
        }
    }

    @CommandChild(label = "get", arguments = {"player"}, description = "pipe.command.gamemode.get.description")
    public static class Get {

        @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.gamemode.get")
        public boolean execute(Command<CommandSender> command) {
            EntitySelectorResult<Player> result = BukkitEntitySelector.parsePlayers(command, "player");
            if (result.hasError()) {
                command.sender().sendMessage(result.getError().from("GameMode").ct());
                return true;
            }

            Player target = result.first();

            command.sender().sendMessage(command.locale().t("pipe.command.gamemode.player_mode").arg("mode", target.getGameMode().name()).ph("player", target).from("GameMode").ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").allowRandom().allowSelf().allowNearest());
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
