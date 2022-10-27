package dev.simplyamazing.jonkcore.Utilities;

import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;

public class ChatUtilities {
    /**
     * Get the name of a User for usage in messaging.
     * @param user : User to get name of
     * @return : name of User
     */
    public static String nameFromUser(IUser user) {
        CommandSender legacy = user.safeGetLegacy();
        if (legacy instanceof ConsoleCommandSender) {
            return autoColour("&4&lCONSOLE");
        } else if (legacy instanceof BlockCommandSender) {
            return autoColour("&c&lCMD-BLOCK");
        } else if (legacy instanceof ProxiedCommandSender) {
            return autoColour("&d&lPROXY");
        } else if (legacy instanceof Player) {
            return autoColour(((Player) legacy).getDisplayName());
        } else {
            return autoColour("&7&lUNKNOWN");
        }
    }

    /**
     * Automatically colour a message.
     * @param message : message to colour
     * @return : coloured message
     */
    public static String autoColour(String message) {
        return message.replaceAll("&", "§");
    }

    /**
     * Automatically strip a message of colour.
     * @param message : message to strip
     * @return : stripped message
     */
    public static String stripColour(String message) {
        return message.replaceAll("§", "&");
    }
}
