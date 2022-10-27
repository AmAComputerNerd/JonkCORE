package dev.simplyamazing.jonkcore.Commands.JonkcoreGroup;

import dev.simplyamazing.jonkcore.JonkCORE;
import dev.simplyamazing.jonkcore.Objects.Command.PluginSubCommand;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import dev.simplyamazing.jonkcore.Objects.User;
import dev.simplyamazing.jonkcore.Utilities.UpdateChecker;

import java.util.List;

public class JonkcoreCheckUpdateCommand extends PluginSubCommand<JonkcoreGroup> {
    public JonkcoreCheckUpdateCommand(JonkcoreGroup group) {
        super(group, "checkupdate", "Check's the plugin GitHub for plugin updates.", "/jonkcore checkupdate", "jonkcore.checkupdates", true);
    }

    @Override
    public void execute(IUser sender, String[] args) {
        if(!(sender instanceof User)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");

        sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&aChecking for updates...");
        UpdateChecker updateChecker = new UpdateChecker(JonkCORE.getInstance());
        updateChecker.run();
        if(updateChecker.isUpToDate()) {
            sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&aNo updates are available.");
        } else {
            sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&aAn update is available! &7" + updateChecker.getLatestVersion());
        }
    }

    @Override
    public List<String> tabComplete(IUser sender, String[] args) {
        if(!(sender instanceof User)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");

        return null;
    }
}
