package dev.simplyamazing.jonkcore.Commands.ChatRoomGroup;

import dev.simplyamazing.jonkcore.JonkCORE;
import dev.simplyamazing.jonkcore.Objects.ChatRoom;
import dev.simplyamazing.jonkcore.Objects.ID;
import dev.simplyamazing.jonkcore.Objects.Command.PluginSubCommand;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IChatRoom;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import dev.simplyamazing.jonkcore.Objects.User;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomRemoveCommand extends PluginSubCommand<ChatRoomGroup> {
    public ChatRoomRemoveCommand(ChatRoomGroup group) {
        super(group, "remove", "Remove a ChatRoom from the server.", "/chatroom remove [name/id]", "jonkcore.chat.remove", true);
    }

    @Override
    public void execute(IUser sender, String[] args) {
        if(!(sender instanceof User u)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");
        if(args.length == 0) {
            sender.sendMessage("&cYou must specify a name or ID for the ChatRoom. Usage: " + getUsage());
            return;
        }

        String nameOrID = args[0];
        ChatRoom cr = JonkCORE.getInstance().getStorage().getChatRoom(new ID(nameOrID));
        if(cr == null) {
            sender.sendMessage("&cA ChatRoom with that name or ID does not exist. Usage: " + getUsage());
            return;
        }
        if(cr.isPermaLocked()) {
            sender.sendMessage("&cThat ChatRoom is a &epersistent &croom and cannot be removed. Usage: " + getUsage());
            return;
        }
        cr.cleanup();
        JonkCORE.getInstance().getStorage().unregisterChatRoom(cr);
        sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&aSuccessfully removed the ChatRoom &8(&7" + cr.getIdentifier().getString() + "&8)&7.");
    }

    @Override
    public List<String> tabComplete(IUser sender, String[] args) {
        if(!(sender instanceof User u)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");

        if(args.length == 0) return null;
        else if(args.length == 1) return getChatRoomNames(u);
        return null;
    }

    /**
     * Logic for getting the list of ChatRoom names.
     *
     * @param sender command sender
     * @return list of ChatRoom names
     */
    private List<String> getChatRoomNames(User sender) {
        List<String> names = new ArrayList<>();
        for(IChatRoom chat : sender.getChatRooms()) {
            if(!(chat instanceof ChatRoom c)) continue;
            names.add(c.getIdentifier().toString());
        }
        return names;
    }
}
