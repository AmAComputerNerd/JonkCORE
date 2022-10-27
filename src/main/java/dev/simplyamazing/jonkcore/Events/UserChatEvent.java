package dev.simplyamazing.jonkcore.Events;

import dev.simplyamazing.jonkcore.Exceptions.ConversionException;
import dev.simplyamazing.jonkcore.Exceptions.UserException;
import dev.simplyamazing.jonkcore.JonkCORE;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IChatRoom;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IJonkPlugin;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class UserChatEvent implements Listener {
    protected final JavaPlugin plugin;
    protected final IJonkPlugin jonkPlugin;

    /**
     * Constructor for UserChatEvent (Overridable Listener).
     * @param plugin : the JavaPlugin that is using this listener.
     */
    public UserChatEvent(final IJonkPlugin jonkPlugin, final JavaPlugin plugin) {
        this.plugin = plugin;
        this.jonkPlugin = jonkPlugin;
    }

    /**
     * Register the listener.
     */
    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Unregister the listener.
     */
    public void unregister() {
        AsyncPlayerChatEvent.getHandlerList().unregister(this);
    }

    /**
     * Event that is called when a player chats.
     * @param e : the AsyncPlayerChatEvent that is called.
     */
    @EventHandler
    public void onUserChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        try {
            IUser u = jonkPlugin.getUserConverter().convert(e.getPlayer());
            for(IChatRoom cr : jonkPlugin.getStorage().getChatRooms()) {
                String triggerKeyword = cr.getTriggerKeyword();
                if(triggerKeyword != null && e.getMessage().startsWith(triggerKeyword) && cr.isSubscribed(u)) {
                    String message = e.getMessage().replaceFirst(triggerKeyword, "");
                    u.sudoSendMessage(cr, message);
                    return;
                }
            }
            u.sudoSendMessage(e.getMessage());
        } catch(ConversionException | UserException exc) {
            exc.printStackTrace();
        }
    }
}
