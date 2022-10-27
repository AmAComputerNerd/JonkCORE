package dev.simplyamazing.jonkcore.Commands.JonkcoreGroup;

import dev.simplyamazing.jonkcore.Objects.Command.PluginCommandGroup;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import dev.simplyamazing.jonkcore.Objects.User;

import java.util.Arrays;
import java.util.List;

public class JonkcoreGroup extends PluginCommandGroup {
    public JonkcoreGroup() {
        super("jcore", "The main command for the JonkCORE plugin.", "/jcore [version / checkupdate / help]", null, true);
    }

    @Override
    public void register() {
        try {
            new JonkcoreVersionCommand(this);
            new JonkcoreCheckUpdateCommand(this);
        } catch(Exception e) {
            e.printStackTrace();
        }
        super.register();
    }

    @Override
    public void unregister() {
        subCommands.clear();
        super.unregister();
    }

    @Override
    public void execute(IUser sender, String[] args) {
        if(!(sender instanceof User)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");
        if(args.length == 0) {
            sender.sendMessage("&cInvalid usage! &7" + getUsage());
            return;
        }

        boolean success = executeSubCommand(args[0], sender, args);
        if(!success) sender.sendMessage("&cInvalid usage! &7" + getUsage());
    }

    @Override
    public List<String> tabComplete(IUser sender, String[] args) {
        if(!(sender instanceof User)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");

        if(args.length == 0) return null;
        else if(args.length == 1) return Arrays.asList("version", "checkupdate", "help");
        return tabCompleteResponse(args[0], sender, args);
    }
}
