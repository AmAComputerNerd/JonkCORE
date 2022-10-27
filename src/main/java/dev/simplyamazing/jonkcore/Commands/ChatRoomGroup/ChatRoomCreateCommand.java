package dev.simplyamazing.jonkcore.Commands.ChatRoomGroup;

import dev.simplyamazing.jonkcore.Exceptions.PermissionRequiredException;
import dev.simplyamazing.jonkcore.JonkCORE;
import dev.simplyamazing.jonkcore.Objects.*;
import dev.simplyamazing.jonkcore.Objects.Command.PluginSubCommand;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChatRoomCreateCommand extends PluginSubCommand<ChatRoomGroup> {
    public ChatRoomCreateCommand(ChatRoomGroup group) {
        super(group, "create", "Create a new ChatRoom.", "/chatroom create [name] (permission) (trigger) (canUseColour) (prefix) (suffix)", "jonkcore.chat.create", true);
    }

    @Override
    public void execute(IUser sender, String[] args) {
        if(!(sender instanceof User)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");
        if(args.length == 0) {
            sender.sendMessage("&cYou must specify a name for the ChatRoom! Usage: " + getUsage());
            return;
        }

        String name = args[0];
        String permission = args.length > 1 ? args[1] : null;
        String trigger = args.length > 2 ? args[2] : null;
        boolean canUseColor = args.length > 3 && Boolean.parseBoolean(args[3]) && sender.hasPermission("jonkcore.chat.colour");
        String prefix = args.length > 4 ? args[4] : "";
        String suffix = args.length > 5 ? args[5] : "";
        // check if room name is taken
        if(JonkCORE.getInstance().getStorage().getChatRoom(name) != null) {
            sender.sendMessage("&cA ChatRoom with that name already exists. Usage: " + getUsage());
            return;
        }
        // create room
        JonkCORE.getInstance().getStorage().registerChatRoom(new ChatRoom(new ID(name), new ChatStyle(prefix, suffix, canUseColor), trigger, permission));
        sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&aSuccessfully created a new ChatRoom with the name &f" + name + "&a.");
        // subscribe to room
        ChatRoom cr = JonkCORE.getInstance().getStorage().getChatRoom(name);
        if(cr == null) {
            sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&cAn error occurred while subscribing you to the ChatRoom. &7Please contact an administrator.");
            return;
        }
        try {
            cr.subscribe(sender);
        } catch(PermissionRequiredException e) {
            sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&cYou do not have permission to join that ChatRoom! &8(Required: &7" + e.getPermission() + "&8)");
        }
    }

    @Override
    public List<String> tabComplete(IUser sender, String[] args) {
        if(!(sender instanceof User)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");

        if(args.length == 0) return null;
        else if(args.length == 1) return Collections.singletonList("exampleName");
        else if(args.length == 2) return Collections.singletonList("jonkcore.chat.examplePermission");
        else if(args.length == 3) return Collections.singletonList("example!");
        else if(args.length == 4) return Arrays.asList("true", "false");
        else if(args.length == 5) return Collections.singletonList("prefix");
        else if(args.length == 6) return Collections.singletonList("suffix");
        return null;
    }
}
