package dev.simplyamazing.jonkcore.Events;

import dev.simplyamazing.jonkcore.JonkCORE;
import dev.simplyamazing.jonkcore.Objects.ID;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IJonkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class UserCreationEvent implements Listener {
    protected JavaPlugin plugin;
    protected IJonkPlugin jonkPlugin;

    /**
     * Constructor for UserCreationEvent (Overridable Listener).
     * @param jonkPlugin the IJonkPlugin that is using this listener.
     * @param plugin the JavaPlugin that is using this listener.
     */
    public UserCreationEvent(final IJonkPlugin jonkPlugin, final JavaPlugin plugin) {
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
        PlayerJoinEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
    }

    /**
     * Event that is called when this plugin is enabled.
     * <br><br>
     * It ensures all online players when the plugin is reloaded have their data loaded.
     * @param e the PluginEnableEvent.
     */
    @EventHandler
    public void onPluginEnabled(PluginEnableEvent e) {
        if(e.getPlugin().equals(plugin)) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(jonkPlugin.getStorage().getUser(new ID(p.getUniqueId())) != null) return; // return if user is already registered
                jonkPlugin.getStorage().registerUser(p);
            }
        }
    }

    /**
     * Event that is called when a player joins the server.
     * @param e the PlayerJoinEvent that is called.
     */
    @EventHandler
    public void onUserJoin(PlayerJoinEvent e) {
        // register the user
        if(jonkPlugin.getStorage().getUser(new ID(e.getPlayer().getUniqueId())) != null) return; // return if user is already registered
        jonkPlugin.getStorage().registerUser(e.getPlayer());
    }

    /**
     * Event that is called when a player leaves the server.
     * @param e the PlayerQuitEvent that is called.
     */
    @EventHandler
    public void onUserLeave(PlayerQuitEvent e) {
        // unregister the user
        if(jonkPlugin.getStorage().getUser(new ID(e.getPlayer().getUniqueId())) == null) return; // return if user is not registered
        jonkPlugin.getStorage().unregisterUser(new ID(e.getPlayer().getUniqueId()));
    }
}
