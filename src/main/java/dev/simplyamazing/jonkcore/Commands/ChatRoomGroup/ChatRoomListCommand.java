package dev.simplyamazing.jonkcore.Commands.ChatRoomGroup;

import dev.simplyamazing.jonkcore.JonkCORE;
import dev.simplyamazing.jonkcore.Objects.ChatRoom;
import dev.simplyamazing.jonkcore.Objects.Command.PluginSubCommand;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IChatRoom;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import dev.simplyamazing.jonkcore.Objects.User;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomListCommand extends PluginSubCommand<ChatRoomGroup> {
    public ChatRoomListCommand(ChatRoomGroup group) {
        super(group, "list", "Get a list of all ChatRooms you are eligible to join.", "/chatroom list", null, true);
    }

    @Override
    public void execute(IUser sender, String[] args) {
        if(!(sender instanceof User)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");

        List<ChatRoom> chatRooms = new ArrayList<>();
        for(IChatRoom chatRoom : JonkCORE.getInstance().getStorage().getChatRooms()) {
            if(!(chatRoom instanceof ChatRoom chatRoom1) || sender.hasChatRoom(chatRoom)); // Skip if not a ChatRoom or if the user is already in the ChatRoom
            else if(chatRoom.getPermission() != null && sender.hasPermission(chatRoom.getPermission())) chatRooms.add(chatRoom1); // include chat rooms the user has permission to join
            else if(chatRoom.getPermission() == null) chatRooms.add(chatRoom1); // don't include chatrooms the user doesn't have permission for
        }
        int size = chatRooms.size();
        sender.sendMessage("&7&m-------------------&r &6&lChatRooms [" + size + "] &7&m-------------------");
        for(ChatRoom chatRoom : chatRooms) {
            sender.sendMessage("&7- &6" + chatRoom.getIdentifier().toString() + " &7(&6" + (chatRoom.getPermission() != null ? chatRoom.getPermission() : "No Permission") + "&7)");
        }
        sender.sendMessage("&7&m-------------------&r &6&lChatRooms [" + size + "] &7&m-------------------");
    }

    @Override
    public List<String> tabComplete(IUser sender, String[] args) {
        if(!(sender instanceof User)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");
        return null;
    }
}