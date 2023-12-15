package fr.lacaleche.pipe.bukkit.modules.command.utils;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BukkitSudoUtils {

    public static boolean processSudo(CommandSender sudoer, Player target, String command) {
        if (command.startsWith("/")) return processSudoCommand(sudoer, target, command);
        else return processSudoChat(sudoer, target, command);
    }

    private static boolean processSudoCommand(CommandSender sudoer, Player target, String command) {
        if (!command.startsWith("/")) return false;
        return true;
    }

    private static boolean processSudoChat(CommandSender sudoer, Player target, String command) {
        try {
            Class<?> chatProcessorClazz = Class.forName("io.papermc.paper.adventure.ChatProcessor");
            Constructor<?> constructor = chatProcessorClazz.getConstructor(MinecraftServer.class, EntityPlayer.class, PlayerChatMessage.class, boolean.class);
            Object chatProcessor = constructor.newInstance(MinecraftServer.getServer(), ((CraftPlayer) target).getHandle(), PlayerChatMessage.a(target.getUniqueId(), command), true);
            chatProcessorClazz.getMethod("process").invoke(chatProcessor);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException exception) {
            Logger.catchThrowable("Unable to process sudo chat: %s", exception, command);
            return false;
        }

        return true;
    }

}
