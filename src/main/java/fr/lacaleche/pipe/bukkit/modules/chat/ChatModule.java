package fr.lacaleche.pipe.bukkit.modules.chat;

import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.chat.commands.ChatCommand;
import fr.lacaleche.pipe.bukkit.modules.chat.listeners.ChatListener;

@AModule(target = ModuleTarget.BUKKIT)
public class ChatModule extends BukkitModule  {

    private ChatManager chatManager;

    public ChatModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        this.chatManager = new ChatManager();
    }
    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerBukkitListener(this, new ChatListener());
    }

    @Override
    public void registerCommands() {
        Pipe.get().getCommandManager().registerNewCommand(this, ChatCommand.class);
    }

    public ChatManager getChatManager() {
        return chatManager;
    }
}
