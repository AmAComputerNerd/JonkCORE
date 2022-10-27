package dev.simplyamazing.jonkcore.Objects.Interfaces;

import java.util.List;

public interface IParentCommand extends ICommand {
    /**
     * Register a new subcommand.
     *
     * @param child the subcommand
     */
    void newCommand(IChildCommand child);

    /**
     * Execute a subcommand based off its name keyword.
     *
     * @param name the command name
     * @param sender the User who executed the command
     * @param args the arguments passed to the command
     * @return whether the command was executed
     */
    boolean executeSubCommand(String name, IUser sender, String[] args);

    /**
     * Get the tab complete response from a subcommand based off its name keyword.
     *
     * @param name the command name
     * @param sender the User who requested this info
     * @param args the current arguments
     * @return the list of strings to display
     */
    List<String> tabCompleteResponse(String name, IUser sender, String[] args);
}
