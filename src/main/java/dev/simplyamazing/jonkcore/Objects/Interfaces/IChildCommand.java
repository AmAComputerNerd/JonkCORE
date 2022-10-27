package dev.simplyamazing.jonkcore.Objects.Interfaces;

import java.util.List;

public interface IChildCommand {
    /**
     * Get the parent command.
     *
     * @return the parent command
     */
    IParentCommand getParent();

    /**
     * Execute the command.
     * <br><br>
     * This method is called when the command is executed, after checks have been performed.
     * It should not be called directly.
     * <br><br>
     * In the rare event this method is called manually, it should be noted that the first argument in the <code>args</code> array does not contain the command name.
     * This is removed within the {@link #onCommand(IUser, String[])} function before being passed to this method.
     *
     * @param sender the sender of the command
     * @param args the arguments of the command
     */
    void execute(IUser sender, String[] args);

    /**
     * Get the tab-completion options for the command.
     * <br><br>
     * This method is called when the command is tab-completed, after checks have been performed.
     * It should not be called directly.
     * <br><br>
     * In the rare event this method is called manually, it should be noted that the first argument in the <code>args</code> array does not contain the command name.
     * This is removed within the {@link #onTabComplete(IUser, String[])} function before being passed to this method.
     *
     * @param sender the sender of the command
     * @param args the arguments of the command
     * @return the tab-completion options for the command
     */
    List<String> tabComplete(IUser sender, String[] args);

    /**
     * Run this command with checks in-place.
     * <br><br>
     * This method is designed to be called by the parent command.
     *
     * @param sender the user who executed the command
     * @param args the arguments passed to the command
     * @return whether the command was executed
     */
    boolean onCommand(IUser sender, String[] args);

    /**
     * Retrieve tab-completion options for the command, with checks.
     *
     * @param sender the user who requested the tab-completion
     * @param args the arguments passed to the command
     * @return the tab-completion options
     */
    List<String> onTabComplete(IUser sender, String[] args);

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
    String getName();

    /**
     * Retrieve the command usage.
     * <br><br>
     * This command usage is automatically formatted.
     *
     * @return command usage
     */
    String getUsage();

    /**
     * Retrieve the command description.
     * <br><br>
     * This command description is used to describe the command in the help menu.
     *
     * @return command description
     */
    String getDescription();

    /**
     * Retrieve the command permission.
     * <br><br>
     * This command permission is used to check whether the user has permission to execute the sub-command.
     * <br><br>
     * If the command permission is null, then the command will be executed presuming the parent command's permission is met.
     *
     * @return command permission
     */
    String getPermission();

    /**
     * Retrieve the message to send when the user does not have permission to execute the command.
     * <br><br>
     * This message is only sent if the command permission is not null.
     * <br><br>
     * This message is automatically formatted.
     *
     * @return the message to send when the user does not have permission to execute the command
     */
    String getPermissionMessage();

    /**
     * Check whether the command is usable by non-Player users.
     *
     * @return whether the command is usable by non-Player users
     */
    boolean canNonPlayerUse();
}
