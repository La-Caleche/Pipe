package fr.lacaleche.pipe.bukkit.modules.command.utils;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import fr.lacaleche.pipe.common.commands.utils.EntitySelectorResult;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BukkitEntitySelector {

    public static EntitySelectorResult<Player> parsePlayers(Command<? extends CommandSender> command, String key) {
        Collection<Player> targets = new ArrayList<>();
        Locale locale = Pipe.getBukkit().getDefaultLocale();

        if (command.sender() instanceof Player player) {
            Client client = Pipe.getBukkit().getClient(player.getUniqueId());
            locale = client.getLocale();
        }

        if (command.args().blank(key)) {
            if (command.sender() instanceof Player player) targets.add(player);
            else {
                return new EntitySelectorResult<>(locale.t("global.only_for_players"));
            }
        } else {
            targets = getPlayers(command, key);
        }

        if (targets.isEmpty()) {
            return new EntitySelectorResult<>(locale.t("global.player_not_found").arg("player", command.args().getString(key)));
        }

        return new EntitySelectorResult<>(targets);
    }

    public static EntitySelectorResult<Client> parseClients(Command<? extends CommandSender> command, String key) {
        Collection<Client> targets = new ArrayList<>();
        Locale locale = Pipe.getBukkit().getDefaultLocale();

        if (command.sender() instanceof Player player) {
            locale = Pipe.getBukkit().getClient(player).getLocale();
        }

        if (command.args().blank(key)) {
            if (command.sender() instanceof Player player) targets.add(Pipe.getBukkit().getClient(player));
            else {
                return new EntitySelectorResult<>(locale.t("global.only_for_players"));
            }
        } else {
            targets = getPlayers(command, key).stream().map(player -> Pipe.getBukkit().getClient(player)).toList();
        }

        if (targets.isEmpty()) {
            return new EntitySelectorResult<>(locale.t("global.player_not_found").arg("player", command.args().getString(key)));
        }

        return new EntitySelectorResult<>(targets);
    }

    private static Collection<Player> getPlayers(Command<? extends CommandSender> command, String key) {
        BukkitPlayerArgument argument = (BukkitPlayerArgument) command.args().get(key);
        String selector = command.args().getString(key);
        Collection<Player> onlinePlayers = new ArrayList<>(Pipe.getBukkit().getPlugin().getServer().getOnlinePlayers());

        if (selector.matches("(@a|all|\\*)") && argument.isAllowAll())
            return onlinePlayers;

        if (selector.matches("(@s|self)") && argument.isAllowSelf() && command.sender() instanceof Player player)
            return List.of(player);

        if (selector.matches("(@r|random)") && argument.isAllowRandom())
            return onlinePlayers.stream().skip((int) (Math.random() * onlinePlayers.size())).limit(1).collect(Collectors.toList());

        if (selector.matches("(@p|nearest)") && argument.isAllowNearest() && command.sender() instanceof Player player)
            return player.getWorld().getNearbyPlayers(player.getLocation(), 48).stream().filter(nearest -> !nearest.getUniqueId().equals(player.getUniqueId())).limit(1).collect(Collectors.toList());

        Player match = onlinePlayers.stream().filter(player -> player.getName().equals(selector)).findFirst().orElse(null);
        return match == null ? List.of() : List.of(match);
    }

}
