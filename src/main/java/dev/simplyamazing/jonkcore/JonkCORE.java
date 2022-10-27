package dev.simplyamazing.jonkcore;

import dev.simplyamazing.jonkcore.Commands.*;
import dev.simplyamazing.jonkcore.Commands.ChatRoomGroup.*;
import dev.simplyamazing.jonkcore.Commands.JonkcoreGroup.JonkcoreGroup;
import dev.simplyamazing.jonkcore.Events.UserChatEvent;
import dev.simplyamazing.jonkcore.Events.UserCreationEvent;
import dev.simplyamazing.jonkcore.Objects.ChatRoom;
import dev.simplyamazing.jonkcore.Objects.GenericUserConverter;
import dev.simplyamazing.jonkcore.Objects.ID;
import dev.simplyamazing.jonkcore.Objects.Index;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IJonkPlugin;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUserConverter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class JonkCORE extends JavaPlugin implements IJonkPlugin {
    // instance variables
    private static JonkCORE instance;
    private Index index;
    private GenericUserConverter userConverter;
    // event variables
    private UserCreationEvent userCreationEvent;
    private UserChatEvent userChatEvent;

    /**
     * Enable the plugin.
     * <br><br>
     * This method is called when the plugin is enabled, and is used to register all commands, events and listeners.
     */
    @Override
    public void onEnable() {
        instance = this;
        try {
            // set instance variables
            this.index = new Index();
            this.userConverter = new GenericUserConverter();
            // load events
            this.userCreationEvent = new UserCreationEvent(this, this);
            userCreationEvent.register();
            this.userChatEvent = new UserChatEvent(this, this);
            userChatEvent.register();
            // register commands
            new ChatCommand().register();
            new SayCommand().register();
            new SayHereCommand().register();
            new JonkcoreGroup().register();
            new ChatRoomGroup().register();
            // fill index with default chat rooms
            index.registerChatRoom(new ChatRoom(new ID("global"), null));
            index.getChatRoom("global").enableParentLock(); // set as persistent room
        } catch(Exception e) {
            // log the exception and disable the plugin
            getLogger().severe("Plugin has failed to load. See stacktrace below.");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    /**
     * Disable the plugin.
     * <br><br>
     * This method is called when the plugin is disabled.
     * It also disables all sub-plugins that depend upon this plugin for functionality.
     */
    @Override
    public void onDisable() {
        // disable other plugins that require this plugin
        for(Plugin p : Bukkit.getPluginManager().getPlugins()) {
            if(p.getDescription().getDepend().contains(getName())) {
                p.getLogger().severe("JonkCORE has been disabled, disabling " + p.getName() + "...");
                Bukkit.getPluginManager().disablePlugin(p);
            }
        }
    }

    /**
     * Get the instance of the plugin.
     * <br><br>
     * This is useful for accessing the plugin's instance variables and methods from other classes.
     *
     * @return instance of the plugin
     */
    public static JonkCORE getInstance() {
        return instance;
    }

    /**
     * Get the UserCreationEvent object for this plugin.
     * <br><br>
     * This is useful for accessing the event's instance variables and methods from other classes.
     * It is possible to register and unregister the event from other classes, though not recommended as it will break core features of the plugin
     *
     * @return UserCreationEvent object
     */
    public UserCreationEvent getUserCreationEvent() {
        return userCreationEvent;
    }

    /**
     * Get the UserChatEvent object for this plugin.
     * <br><br>
     * This is useful for accessing the event's instance variables and methods from other classes.
     * It is possible to register and unregister the event from other classes, though not recommended as it will break core features of the plugin
     *
     * @return UserChatEvent object
     */
    public UserChatEvent getUserChatEvent() {
        return userChatEvent;
    }

    /**
     * Retrieve a list of all sub-plugins that depend on this plugin.
     * <br><br>
     * This is useful for accessing the sub-plugin's instance variables and methods from other classes.
     * It is possible to register and unregister the sub-plugins from other classes, though not recommended as it will break core features of the plugin
     * <br><br>
     * The returned list is a list of Plugin objects, which can be cast to {@link IJonkPlugin} and {@link JavaPlugin} respectively.
     *
     * @return list of sub-plugins
     */
    public List<Plugin> getJonkPlugins() {
        List<Plugin> jonkPlugins = new ArrayList<>();
        for(Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if(plugin instanceof IJonkPlugin) {
                jonkPlugins.add(plugin);
            }
        }
        return jonkPlugins;
    }

    /**
     * Retrieve the UserConverter object of the sub-plugin.
     * <br><br>
     * The UserConverter object is used to convert between Bukkit's Player objects, other generic JonkCORE User objects and hashcodes to the sub-plugin's
     * variant of the User object. It is particularly useful in cross-plugin communication, as it enables different sub-plugins to convert generic User objects back
     * and forth.
     *
     * @return UserConverter object of the sub-plugin
     */
    @Override
    public IUserConverter getUserConverter() {
        return userConverter;
    }

    /**
     * Retrieve the Storage object of the sub-plugin.
     * <br><br>
     * This object acts as the soft-storage system within the sub-plugin, holding information about the sub-plugin's users and other data.
     *
     * @return Storage object of the sub-plugin
     */
    @Override
    public Index getStorage() {
        return index;
    }

    /**
     * Retrieve the prefix all messages sent by the sub-plugin will have.
     * <br><br>
     * This is useful for differentiating chat messages coming from different plugins, and it's position as the first line within a message is absolute.
     *
     * @return prefix of the sub-plugin
     */
    @Override
    public String getPrefix() {
        return "§8[§6JonkCORE§8]§r ";
    }

    /**
     * Retrieve the version number of the sub-plugin, normally in the format of (major).(minor).(patch).
     * <br><br>
     * The version number is usually used as both a reference for the UpdateChecker to check for updates, and to identify the plugin in the console.
     * In most situations, this should link to the version number in the plugin.yml file.
     *
     * @return version number of the sub-plugin
     */
    @Override
    public String getVersion() {
        return getDescription().getVersion();
    }

    /**
     * Retrieve the author of the sub-plugin.
     * <br><br>
     * In most situations, this should link to the author in the plugin.yml file.
     * In the case of multiple authors, this should return authors separated by a comma.
     *
     * @return author of the sub-plugin
     */
    @Override
    public String getAuthor() {
        StringBuilder authors = new StringBuilder();
        if(getDescription().getAuthors().size() == 1) {
            authors.append(getDescription().getAuthors().get(0));
        } else {
            for(int i = 0; i < getDescription().getAuthors().size(); i++) {
                if(i == 0) {
                    authors.append(getDescription().getAuthors().get(i));
                } else if(i == getDescription().getAuthors().size() - 1) {
                    authors.append(" and ").append(getDescription().getAuthors().get(i));
                } else {
                    authors.append(", ").append(getDescription().getAuthors().get(i));
                }
            }
        }
        return authors.toString();
    }

    /**
     * Retrieve the GitHub page of the sub-plugin.
     * <br><br>
     * This is most commonly used as a reference for the UpdateChecker to locate the latest release version.
     *
     * @return GitHub page of the sub-plugin
     */
    @Override
    public URL getRepository() {
        try {
            return new URL("https://raw.githubusercontent.com/ThatComputerNerd/JonkCORE/main/src");
        } catch(MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
