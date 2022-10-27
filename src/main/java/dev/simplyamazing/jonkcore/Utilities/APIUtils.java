package dev.simplyamazing.jonkcore.Utilities;

import dev.simplyamazing.jonkcore.JonkCORE;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IChatRoom;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IJonkPlugin;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class APIUtils {
    public static HashMap<IJonkPlugin, IChatRoom> getAllChatRooms() {
        HashMap<IJonkPlugin, IChatRoom> chatRooms = new HashMap<>();
        for(Plugin plugin : JonkCORE.getInstance().getJonkPlugins()) {
            ((IJonkPlugin)plugin).getStorage().getChatRooms().forEach(chatRoom -> chatRooms.put((IJonkPlugin)plugin, chatRoom));
        }
        return chatRooms;
    }
}
