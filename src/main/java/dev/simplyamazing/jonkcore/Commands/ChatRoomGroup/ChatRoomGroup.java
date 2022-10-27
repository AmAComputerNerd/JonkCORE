package dev.simplyamazing.jonkcore.Commands.ChatRoomGroup;

import dev.simplyamazing.jonkcore.Objects.Command.PluginCommandGroup;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;

import java.util.Arrays;
import java.util.List;

public class ChatRoomGroup extends PluginCommandGroup {
    public ChatRoomGroup() {
        super("chatroom", "Execute actions relevant to ChatRooms.", "/chatroom [join / create / remove / list] (args)", null, true);
    }

    @Override
    public void register() {
        try {
            new ChatRoomCreateCommand(this);
            new ChatRoomJoinCommand(this);
            new ChatRoomRemoveCommand(this);
            new ChatRoomListCommand(this);
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
        if(args.length == 0) {
            sender.sendMessage("&cInvalid usage! &7" + getUsage());
            return;
        }

        boolean success = executeSubCommand(args[0], sender, args);
        if(!success) sender.sendMessage("&cInvalid usage! &7" + getUsage());
    }

    @Override
    public List<String> tabComplete(IUser sender, String[] args) {
        if(args.length == 0) return null;
        else if(args.length == 1) return Arrays.asList("join", "create", "remove", "list");
        return tabCompleteResponse(args[0], sender, args);
    }
}
