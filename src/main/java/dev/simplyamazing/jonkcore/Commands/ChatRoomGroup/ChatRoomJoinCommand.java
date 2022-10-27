package dev.simplyamazing.jonkcore.Commands.ChatRoomGroup;

import dev.simplyamazing.jonkcore.Exceptions.PermissionRequiredException;
import dev.simplyamazing.jonkcore.JonkCORE;
import dev.simplyamazing.jonkcore.Objects.ChatRoom;
import dev.simplyamazing.jonkcore.Objects.Command.PluginSubCommand;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IChatRoom;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import dev.simplyamazing.jonkcore.Objects.User;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomJoinCommand extends PluginSubCommand<ChatRoomGroup> {
    public ChatRoomJoinCommand(ChatRoomGroup group) {
        super(group, "join", "Join a ChatRoom.", "/chatroom join [name/id]", null, true);
    }

    @Override
    public void execute(IUser sender, String[] args) {
        if(!(sender instanceof User)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");
        if(args.length == 0) {
            sender.sendMessage("&cInvalid usage! &7" + getUsage());
            return;
        }

        ChatRoom cr = JonkCORE.getInstance().getStorage().getChatRoom(args[0]);
        if(cr == null) {
            sender.sendMessage("&cInvalid ChatRoom &8(&7" + args[0] + "&8). &7You can find a list of valid rooms with &e&l/chatroom list&7.");
            return;
        }
        if(sender.hasChatRoom(cr)) {
            sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&cYou are already in that ChatRoom! &7You may switch to different rooms with &e&l/chat [name/id]&7.");
            return;
        }
        try {
            cr.subscribe(sender);
            sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&aYou have successfully joined the ChatRoom &8(&7" + cr.getIdentifier().getString() + "&8)&7.");
        } catch(PermissionRequiredException e) {
            sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&cYou do not have permission to join that ChatRoom! &8(Required: &7" + e.getPermission() + "&8)");
        }
    }

    @Override
    public List<String> tabComplete(IUser sender, String[] args) {
        if(!(sender instanceof User u)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");

        if(args.length == 0) return null;
        else if(args.length == 1) return getChatRoomNames(u);
        else return null;
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
