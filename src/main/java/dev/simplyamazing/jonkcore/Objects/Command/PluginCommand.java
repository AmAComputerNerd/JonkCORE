package dev.simplyamazing.jonkcore.Objects.Command;

import dev.simplyamazing.jonkcore.JonkCORE;
import dev.simplyamazing.jonkcore.Objects.ID;
import dev.simplyamazing.jonkcore.Objects.Interfaces.ICommand;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import dev.simplyamazing.jonkcore.Objects.PluginObject;
import dev.simplyamazing.jonkcore.Objects.User;
import org.bukkit.command.*;

import java.util.List;

public abstract class PluginCommand extends PluginObject implements ICommand, CommandExecutor, TabCompleter {
    private final String cmd;
    private final String desc;
    private final String usage;
    private final String permission;
    private final boolean canNonPlayerUse;

    /**
     * Initialisation for a new PluginCommand object.
     * @param cmd command name
     * @param desc command description
     * @param usage command usage
     * @param permission command permission
     * @param canNonPlayerUse whether non-players can use the command
     */
    public PluginCommand(String cmd, String desc, String usage, String permission, boolean canNonPlayerUse) {
        super(new ID(cmd), true);
        this.cmd = cmd;
        this.desc = desc;
        this.usage = usage;
        this.permission = permission;
        this.canNonPlayerUse = canNonPlayerUse;
        enableParentLock();
    }

    /**
     * Bukkit command executor.
     * @param sender command sender
     * @param command command
     * @param label command label
     * @param args command arguments
     * @return whether the command was executed
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = User.fetch(sender);
        if(sender instanceof org.bukkit.entity.Player) {
            if(permission == null || user.hasPermission(permission)) {
                execute(user, args);
            } else {
                user.sendMessage("&cYou do not have permission to use this command!");
            }
        } else {
            if(canNonPlayerUse) {
                execute(user, args);
            } else {
                user.sendMessage("&cYou must be a player to use this command!");
            }
        }
        return true;
    }

    /**
     * Bukkit tab completer.
     * @param sender command sender
     * @param command command
     * @param label command label
     * @param args command arguments
     * @return tab completion list
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        User user = User.fetch(sender);
        return tabComplete(user, args);
    }

    /**
     * Get the command name.
     * @return command name
     */
    public String getName() {
        return cmd;
    }

    /**
     * Get the command description.
     * @return command description
     */
    public String getDescription() {
        return desc;
    }

    /**
     * Get the command usage.
     * @return command usage
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Get the command permission.
     * @return command permission
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Check if non-players can use the command.
     * @return whether non-players can use the command
     */
    public boolean canNonPlayerUse() {
        return canNonPlayerUse;
    }

    /**
     * Check if a user can use the command.
     * @param user user
     * @return whether the user can use the command
     */
    public boolean checkPermission(IUser user) {
        return user.hasPermission(permission);
    }

    /**
     * Register this command.
     */
    public void register() {
        try {
            JonkCORE.getInstance().getCommand(cmd).setExecutor(this);
            JonkCORE.getInstance().getLogger().info("Successfully registered command: " + cmd);
        } catch(NullPointerException e) {
            JonkCORE.getInstance().getLogger().warning("The command '/" + cmd + "' is not registered in the plugin.yml! It has not been loaded.");
        }
    }

    /**
     * Unregister this command.
     */
    public void unregister() {
        try {
            JonkCORE.getInstance().getCommand(cmd).setExecutor(null);
            JonkCORE.getInstance().getLogger().info("Successfully unregistered command: " + cmd);
        } catch(NullPointerException e) {
            JonkCORE.getInstance().getLogger().warning("The command '/" + cmd + "' is not registered in the plugin.yml! It has not been loaded.");
        }
    }
}
