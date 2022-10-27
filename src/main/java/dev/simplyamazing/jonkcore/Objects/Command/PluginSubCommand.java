package dev.simplyamazing.jonkcore.Objects.Command;

import dev.simplyamazing.jonkcore.Objects.ID;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IChildCommand;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import dev.simplyamazing.jonkcore.Objects.PluginObject;
import dev.simplyamazing.jonkcore.Objects.User;

import java.util.List;

public abstract class PluginSubCommand<T extends PluginCommandGroup> extends PluginObject implements IChildCommand {
    protected final T group;
    protected final String triggerKeyword;
    protected final String description;
    protected final String usage;
    protected final String requiredPermission;
    protected final boolean canNonPlayerUse;

    /**
     * Initialise a new subcommand.
     * @param group the group this subcommand belongs to
     * @param triggerKeyword the trigger keyword
     * @param usage how to use the subcommand
     */
    public PluginSubCommand(T group, String triggerKeyword, String usage) {
        this(group, triggerKeyword, null, usage, null, true);
    }

    /**
     * Initialise a new subcommand with all custom values.
     * @param group the group this subcommand belongs to
     * @param triggerKeyword the trigger keyword
     * @param description the description of the subcommand
     * @param usage how to use the subcommand
     * @param requiredPerm the required permission for the subcommand
     * @param canNonPlayerUse whether non-Player Users can execute this subcommand
     */
    public PluginSubCommand(T group, String triggerKeyword, String description, String usage, String requiredPerm, boolean canNonPlayerUse) {
        super(new ID(group.getName() + "-" + triggerKeyword));
        this.group = group;
        this.triggerKeyword = triggerKeyword;
        this.description = description;
        this.usage = usage;
        this.requiredPermission = requiredPerm;
        this.canNonPlayerUse = canNonPlayerUse;
        group.newCommand(this);
    }

    /**
     * Run this command with checks in-place.
     * <br><br>
     * This method is designed to be called by the parent command.
     *
     * @param sender the user who executed the command
     * @param args the arguments passed to the command
     * @return whether the command was executed
     */
    public boolean onCommand(IUser sender, String[] args) {
        if(requiredPermission != null && !sender.hasPermission(requiredPermission)) {
            sender.sendMessage("&cYou do not have permission to use this command.");
            return true;
        }

        if(!this.canNonPlayerUse && !sender.isPlayer()) {
            sender.sendMessage("&cYou must be a player to use this command.");
            return true;
        }

        String[] arguments = new String[args.length-1];
        System.arraycopy(args, 1, arguments, 0, args.length-1);

        execute(sender, arguments);
        return false;
    }

    /**
     * Retrieve tab-completion options for the command, with checks.
     *
     * @param sender the user who requested the tab-completion
     * @param args the arguments passed to the command
     * @return the tab-completion options
     */
    public List<String> onTabComplete(IUser sender, String[] args) {
        if(requiredPermission != null && !sender.hasPermission(requiredPermission)) {
            return null;
        }

        if(!this.canNonPlayerUse && !sender.isPlayer()) {
            return null;
        }

        String[] arguments = new String[args.length-1];
        System.arraycopy(args, 1, arguments, 0, args.length-1);

        return tabComplete(sender, arguments);
    }

    /**
     * Get the parent command.
     *
     * @return the parent command
     */
    public T getParent() {
        return group;
    }

    /**
     * Retrieve the command name.
     * <br><br>
     * This command name is used to identify the sub-command, and is also the execution phrase for the sub-command.
     * <br><br>
     * For example, if the command name is "bar" and the parent command name is "foo", then the command will be executed by typing "/foo bar" in chat.
     * <br><br>
     * Command names must be unique to their sub-command, and are limited to letters, numbers, and underscores.
     *
     * @return command name
     */
    public String getName() {
        return triggerKeyword;
    }

    /**
     * Retrieve the command description.
     * <br><br>
     * This command description is used to describe the command in the help menu.
     *
     * @return command description
     */
    public String getDescription() {
        return (description == null || description.equals("")) ? group.getDescription() : description;
    }

    /**
     * Retrieve the command usage.
     * <br><br>
     * This command usage is automatically formatted.
     *
     * @return command usage
     */
    public String getUsage() {
        return (usage == null || usage.equals("")) ? group.getUsage() : usage;
    }

    /**
     * Retrieve the command permission.
     * <br><br>
     * This command permission is used to check whether the user has permission to execute the sub-command.
     * <br><br>
     * If the command permission is null, then the command will be executed presuming the parent command's permission is met.
     *
     * @return command permission
     */
    public String getPermission() {
        return requiredPermission;
    }

    /**
     * Retrieve the message to send when the user does not have permission to execute the command.
     * <br><br>
     * This message is only sent if the command permission is not null.
     * <br><br>
     * This message is automatically formatted.
     *
     * @return the message to send when the user does not have permission to execute the command
     */
    public String getPermissionMessage() {
        return "&cYou do not have permission to use this command.";
    }

    /**
     * Check whether the command is usable by non-Player users.
     *
     * @return whether the command is usable by non-Player users
     */
    public boolean canNonPlayerUse() {
        return canNonPlayerUse;
    }
}
