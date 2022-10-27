package dev.simplyamazing.jonkcore.Commands;

import dev.simplyamazing.jonkcore.Objects.ChatRoom;
import dev.simplyamazing.jonkcore.Objects.Command.PluginCommand;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import dev.simplyamazing.jonkcore.Objects.User;

import java.util.Collections;
import java.util.List;

public class SayHereCommand extends PluginCommand {
    public SayHereCommand() {
        super("sayhere", "Speak a message into your currently focused chat.", "/sayhere [message]", null, true);
    }

    @Override
    public void execute(IUser sender, String[] args) {
        if(!(sender instanceof User u)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");
        if(args.length == 0) {
            sender.sendMessage(getUsage());
            return;
        }

        ChatRoom chatRoom = u.getFocusedChatRoom();
        if(chatRoom == null) {
            sender.sendMessage("&cYou are not currently focused on a chat room.");
            return;
        }
        StringBuilder message = new StringBuilder();
        for(String arg : args) {
            message.append(arg).append(" ");
        }
        chatRoom.sendMessage(u, message.toString());
    }

    @Override
    public List<String> tabComplete(IUser sender, String[] args) {
        if(!(sender instanceof User)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");

        if(args.length == 0) return null;
        else if(args.length == 1) return Collections.singletonList("message");
        return null;
    }
}
