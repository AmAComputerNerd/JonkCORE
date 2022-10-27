package dev.simplyamazing.jonkcore.Commands;

import dev.simplyamazing.jonkcore.Exceptions.UserException;
import dev.simplyamazing.jonkcore.JonkCORE;
import dev.simplyamazing.jonkcore.Objects.ChatRoom;
import dev.simplyamazing.jonkcore.Objects.Command.PluginCommand;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IChatRoom;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import dev.simplyamazing.jonkcore.Objects.User;

import java.util.ArrayList;
import java.util.List;

public class ChatCommand extends PluginCommand {
    public ChatCommand() {
        super("chat", "Change your currently focused ChatRoom.", "/chat [name/id]", null, true);
    }

    @Override
    public void execute(IUser sender, String[] args) {
        if(!(sender instanceof User u)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");
        if(args.length == 0) {
            sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&cYou must specify a ChatRoom to focus upon! Usage: " + getUsage());
            return;
        }
        for(IChatRoom chat : sender.getChatRooms()) {
            if(!(chat instanceof ChatRoom c)) continue;
            if(c.getIdentifier().toString().equals(args[0])) {
                if(c.equals(sender.getFocusedChatRoom())) {
                    sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&cYou are already focused on this ChatRoom!");
                    return;
                }
                try {
                    sender.setFocusedChatRoom(c);
                    sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&aYou are now focused on the ChatRoom &e" + c.getIdentifier().toString() + "&a.");
                    return;
                } catch(UserException e) {
                    sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&cUh oh! Something went wrong! Please check to ensure you have access to the ChatRoom you are trying to focus on.");
                    e.printStackTrace();
                    return;
                }
            }
        }
        sender.sendMessage(JonkCORE.getInstance().getPrefix() + "&cYou are not subscribed to a ChatRoom with the ID &e" + args[0] + "&c!");
    }

    @Override
    public List<String> tabComplete(IUser sender, String[] args) {
        if(!(sender instanceof User u)) throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + sender.getClass().getName() + ")");

        if(args.length == 0) return null;
        else if(args.length == 1) return getChatRoomNames(u);
        return new ArrayList<>();
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
