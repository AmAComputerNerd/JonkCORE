package dev.simplyamazing.jonkcore.Commands.JonkcoreGroup;

import dev.simplyamazing.jonkcore.JonkCORE;
import dev.simplyamazing.jonkcore.Objects.Command.PluginSubCommand;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import dev.simplyamazing.jonkcore.Objects.User;

import java.util.List;

public class JonkcoreVersionCommand extends PluginSubCommand<JonkcoreGroup> {
    public JonkcoreVersionCommand(JonkcoreGroup group) {
        super(group, "version", "Shows the version of the JonkCORE core plugin.", "/jonkcore version", null, true);
    }

    @Override
    public void execute(IUser sender, String[] args) {
        if(!(sender instanceof User)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");

        sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&aThe current version of JonkCORE is &f" + JonkCORE.getInstance().getVersion() + "&a.");
    }

    @Override
    public List<String> tabComplete(IUser sender, String[] args) {
        if(!(sender instanceof User)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");

        return null;
    }
}
