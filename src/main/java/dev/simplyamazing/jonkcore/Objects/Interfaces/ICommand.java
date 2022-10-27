package dev.simplyamazing.jonkcore.Objects.Interfaces;

import java.util.List;

public interface ICommand {
    /**
     * Execute the command.
     * <br><br>
     * This method is called when the command is executed, after checks have been performed.
     * It should not be called directly.
     * <br><br>
     * In the event that the command is a group command, it should always be suffixed with:
     * <br><br>
     * <code>
     *     boolean success = executeSubCommand(args[0], sender, args);
     *     <br>
     *     if(!success) sender.sendMessage("&cInvalid usage! &7" + getUsage());
     * </code>
     * <br><br>
     * This may be subject to change in the future, but this solution remains effective for now.
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
     * In the event that the command is a group command, it should always be suffixed with:
     * <br><br>
     * <code>
     *     return tabCompleteResponse(args[0], sender, args);
     * </code>
     * <br><br>
     * This may be subject to change in the future, but this solution remains effective for now.
     *
     * @param sender the sender of the command
     * @param args the arguments of the command
     * @return the tab-completion options for the command
     */
    List<String> tabComplete(IUser sender, String[] args);

    /**
     * Retrieve the command name.
     * <br><br>
     * This command name is used to identify the command, and is also the execution phrase for the command.
     * <br><br>
     * For example, if the command name is "test", then the command will be executed by typing "/test" in chat.
     * <br><br>
     * As such, the command name should be unique and is limited to letters, numbers, and underscores.
     *
     * @return command name
     */
    String getName();

    /**
     * Retrieve the command description.
     * <br><br>
     * This command description is used to describe the command in the help menu.
     * <br><br>
     * For example, if the command description is "This is a test command", then the command will be described as "This is a test command" in the help menu.
     * <br><br>
     * As such, the command description should ideally be short and concise.
     *
     * @return command description
     */
    String getDescription();

    /**
     * Retrieve the command usage.
     * <br><br>
     * This command usage is used to describe how to use the command to the user.
     * <br>
     * It is also shown when the user types the command incorrectly.
     *
     * @return command usage
     */
    String getUsage();

    /**
     * Retrieve the command permission.
     * <br><br>
     * This command permission is used to determine whether the user has permission to use the command.
     * <br><br>
     * The permission is checked through the {@link dev.simplyamazing.jonkcore.Utilities.PermissionUtils#checkAny(IUser, String)} method.
     * As such, any parent permissions will also be checked.
     *
     * @return command permission
     */
    String getPermission();

    /**
     * Check if non-players can use the command.
     * <br><br>
     * This method is used to determine whether non-players can use the command.
     * <br><br>
     * If this is false, the command will fail to execute if the User is not a player.
     *
     * @return whether non-players can use the command
     */
    boolean canNonPlayerUse();

    /**
     * Check if a user can use the command.
     * <br><br>
     * This method is used to determine whether a user can use the command.
     * <br><br>
     * This method is called before the command is executed, and if it returns false, the command will not be executed.
     *
     * @param user user
     * @return whether the user can use the command
     */
    boolean checkPermission(IUser user);

    /**
     * Register this command.
     * <br><br>
     * This method will register the command with the server.
     * The command name will be used as the keyword, and as such must be the same as in the <code>plugin.yml</code> file.
     * <br><br>
     * This method is called when the plugin is enabled, and should not be called manually.
     */
    void register();

    /**
     * Unregister this command.
     * <br><br>
     * This method will unregister the command with the server.
     * The command name will be used as the keyword, and as such must be the same as in the <code>plugin.yml</code> file.
     * <br><br>
     * While this method is safe to call, it should be avoided if possible as this can result in unexpected errors.
     */
    void unregister();
}
