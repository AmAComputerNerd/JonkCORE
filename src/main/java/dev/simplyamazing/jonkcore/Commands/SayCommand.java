package dev.simplyamazing.jonkcore.Commands;

import dev.simplyamazing.jonkcore.JonkCORE;
import dev.simplyamazing.jonkcore.Objects.ChatRoom;
import dev.simplyamazing.jonkcore.Objects.Command.PluginCommand;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IChatRoom;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import dev.simplyamazing.jonkcore.Objects.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SayCommand extends PluginCommand {
    public SayCommand() {
        super("say", "Speak in a ChatRoom regardless of focus.", "/say [chatroom] [message]", "jonkcore.say", true);
    }

    @Override
    public void execute(IUser sender, String[] args) {
        if(!(sender instanceof User u)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");
        if(args.length == 0) {
            sender.sendMessage("&cYou must specify a ChatRoom to speak in! Usage: " + getUsage());
            return;
        } else if(args.length == 1) {
            sender.sendMessage("&cYou must specify a message to send! Usage: " + getUsage());
            return;
        }

        ChatRoom chatroom = JonkCORE.getInstance().getStorage().getChatRoom(args[0]);
        StringBuilder message = new StringBuilder();
        for(int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        if(chatroom == null) {
            sender.sendMessage("&cThat ChatRoom does not exist! Usage: " + getUsage());
            return;
        }

        if(!chatroom.isSubscribed(u) && !u.hasPermission("jonkcore.say.bypassChatMembership")) {
            sender.sendMessage("&cYou are not subscribed to that ChatRoom! Usage: " + getUsage());
            return;
        }
        chatroom.sendMessage(u, message.toString());
    }

    @Override
    public List<String> tabComplete(IUser sender, String[] args) {
        if(!(sender instanceof User u)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");

        if(args.length == 0) return null;
        else if(args.length == 1) return getChatRoomNames(u);
        else return Collections.singletonList("message");
    }

    /**
     * Logic for getting the list of ChatRoom names.
     *
     * @param sender command sender
     * @return list of ChatRoom names
     */
    private List<String> getChatRoomNames(User sender) {
        List<String> names = new ArrayList<>();
        if(sender.hasPermission("jonkcore.say.bypassChatMembership")) {
            for(IChatRoom chatroom : JonkCORE.getInstance().getStorage().getChatRooms()) {
                if(!(chatroom instanceof ChatRoom c)) continue;
                names.add(c.getIdentifier().toString());
            }
        } else {
            for(IChatRoom chat : sender.getChatRooms()) {
                if(!(chat instanceof ChatRoom c)) continue;
                names.add(c.getIdentifier().toString());
            }
        }
        return names;
    }
}
