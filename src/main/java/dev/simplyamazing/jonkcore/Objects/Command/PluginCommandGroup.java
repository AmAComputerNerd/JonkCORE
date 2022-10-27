package dev.simplyamazing.jonkcore.Objects.Command;

import dev.simplyamazing.jonkcore.Objects.Interfaces.IChildCommand;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IParentCommand;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import dev.simplyamazing.jonkcore.Objects.User;

import java.util.ArrayList;
import java.util.List;

public abstract class PluginCommandGroup extends PluginCommand implements IParentCommand {
    // list of registered subcommands
    protected List<PluginSubCommand> subCommands;

    /**
     * Initialise a new group command.
     * @param cmd the command string
     * @param desc the description string
     * @param usage the usage string
     * @param perm the permission to check, or null if no check
     * @param canNonPlayerUse whether non-Player Users can execute this command
     */
    public PluginCommandGroup(String cmd, String desc, String usage, String perm, boolean canNonPlayerUse) {
        super(cmd, desc, usage, perm, canNonPlayerUse);
        this.subCommands = new ArrayList<>();
    }

    /**
     * Register a new subcommand.
     * @param psc the subcommand
     */
    public void newCommand(IChildCommand psc) {
        if(!(psc instanceof PluginSubCommand)) throw new IllegalArgumentException("Provided ChildCommand is a differing implementation than required (Expected: " + PluginSubCommand.class.getName() + ", Provided: " + psc.getClass().getName() + ")");
        subCommands.add((PluginSubCommand)psc);
    }

    /**
     * Execute a subcommand.
     * @param trigger the command trigger keyword
     * @param sender the User who executed the command
     * @param args the arguments passed to the command
     * @return whether the command was executed
     */
    public boolean executeSubCommand(String trigger, IUser sender, String[] args) {
        PluginSubCommand sub = null;
        for(PluginSubCommand psc : subCommands) {
            if(psc.getName().equalsIgnoreCase(trigger)) {
                sub = psc;
                break;
            }
        }
        if(sub == null) return false;
        if(!(sender instanceof User)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");
        sub.onCommand(sender, args);
        return true;
    }

    /**
     * Get the tab complete response from a subcommand.
     * @param trigger the trigger argument for the subcommand
     * @param sender the User who requested this info
     * @param args the current arguments
     * @return the list of strings to display
     */
    public List<String> tabCompleteResponse(String trigger, IUser sender, String[] args) {
        PluginSubCommand sub = null;
        for(PluginSubCommand psc : subCommands) {
            if(psc.getName().equalsIgnoreCase(trigger)) {
                sub = psc;
                break;
            }
        }
        if(sub == null) return null;
        if(!(sender instanceof User)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");
        return sub.onTabComplete(sender, args);
    }
}
