package dev.simplyamazing.jonkcore.Objects;

import dev.simplyamazing.jonkcore.Exceptions.PermissionRequiredException;
import dev.simplyamazing.jonkcore.JonkCORE;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IChatRoom;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IChatStyling;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import dev.simplyamazing.jonkcore.Utilities.ChatUtilities;
import dev.simplyamazing.jonkcore.Utilities.PermissionUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom extends PluginObject implements IChatRoom {
    private final IChatStyling chatStyle;
    private final String trigger;
    private final String permission;
    private final List<User> subscribedUsers;

    private boolean roomLock;
    private boolean roomMute;

    /**
     * Instantiate default values.
     * @param id the UUID of the object
     * @param permission the permission required to join the chat room
     */
    public ChatRoom(final ID id, final String permission) {
        this(id, new ChatStyle(), null, permission);
    }

    /**
     * Instantiate default values.
     * @param id the UUID of the object
     * @param chatStyle the chat style of the chat room
     * @param trigger the trigger keyword for the chat room
     * @param permission the permission required to join the chat room
     */
    public ChatRoom(final ID id, final IChatStyling chatStyle, final String trigger, final String permission) {
        super(id, true, false, false);
        this.chatStyle = chatStyle;
        this.trigger = trigger;
        this.permission = permission;
        this.subscribedUsers = new ArrayList<>();
        this.roomLock = false;
        this.roomMute = false;
    }

    /**
     * Retrieve the chat styling for this ChatRoom.
     * <br><br>
     * The ChatStyling object contains all formatting and styling options for this ChatRoom.
     * <br>
     * This often includes but is not limited to a prefix, suffix, default colour and permission standards.
     * @return ChatStyling object
     */
    @Override
    public IChatStyling getChatStyle() {
        return chatStyle;
    }

    /**
     * Retrieve the trigger keyword that will automatically send a following message to this ChatRoom.
     * <br><br>
     * The keyword, which normally ends in a symbol character, is unique to a ChatRoom, and will result in any message sent with the prefixing keyword to be
     * redirected to this ChatRoom.
     * <br>
     * This will only happen if the User sending the message is subscribed to the ChatRoom and maintains permission to speak in it.
     * @return String keyword
     */
    @Override
    public String getTriggerKeyword() {
        return trigger;
    }

    /**
     * Retrieve the lowest permission required to subscribe to and speak in this ChatRoom.
     * <br><br>
     * The permission is checked upon subscribing to the room and each time before the User chats in the room.
     * <br>
     * If a User no longer has permission to chat and see the room, they will be automatically unsubscribed and have their focused room reset if it was set to this ChatRoom.
     * @return String permission
     */
    @Override
    public String getPermission() {
        return permission;
    }

    /**
     * Retrieve the list of Users subscribed to this ChatRoom.
     * @return List of Users
     */
    @Override
    public List<IUser> getSubscribedUsers() {
        return new ArrayList<>(subscribedUsers);
    }

    /**
     * Send a message to all Users subscribed to this ChatRoom.
     * <br><br>
     * This message will be automatically formatted and styled according to the ChatStyling object of this ChatRoom.
     * This method will bypass the mute status of the ChatRoom.
     * <br><br>
     * The alternative {@link #sendMessage(IUser, String)} method should instead be used if trying to send a message from a specific User.
     *
     * @param message Message to send
     */
    @Override
    public void sendMessage(String message) {
        for(IUser user : subscribedUsers) {
            user.sendMessage(chatStyle.formatMessage(message));
        }
    }

    /**
     * Send a message to all Users subscribed to this ChatRoom, prefixed with the User's name.
     * <br><br>
     * This message will be automatically formatted and styled according to the ChatStyling object of this ChatRoom.
     * This method <i>can</i> bypass the mute status of the ChatRoom, depending on the User's permission.
     * <br><br>
     * The alternative {@link #sendMessage(String)} method should instead be used if trying to send a generic message.
     * @param sender  User sending the message
     * @param message Message to send
     */
    @Override
    public void sendMessage(IUser sender, String message) {
        if(roomIsMuted() && !sender.hasPermission("jonkcore.chatroom." + getIdentifier().toString().replaceAll(" ", "-") + ".bypassmute")) {
            sender.sendMessage("&cThis chat room is currently muted.");
            return;
        }
        for(IUser user : subscribedUsers) {
            if(user.equals(sender)) user.sendMessage(chatStyle.getPrefix() + " &a&lYOU: &r" + chatStyle.formatMessageAnonymously(message) + " " + chatStyle.getSuffix());
            else user.sendMessage(chatStyle.getPrefix() + " " + ChatUtilities.nameFromUser(sender) + ": &r" + chatStyle.formatMessageAnonymously(message) + " " + chatStyle.getSuffix());
        }
    }

    /**
     * Subscribe a User to this ChatRoom.
     * <br><br>
     * The User will be automatically checked for permission to subscribe to the ChatRoom, and a {@link PermissionRequiredException} will be thrown if the User does not pass this check.
     * <br>
     * Upon successfully joining a ChatRoom, the User will be notified of their subscription and the objects should be updated to reflect the change.
     * <br><br>
     * If this ChatRoom is currently locked and a User does not have permission to bypass this, a {@link PermissionRequiredException} will also be thrown.
     *
     * @param user User to subscribe
     * @throws PermissionRequiredException If the User does not have permission to subscribe to the ChatRoom OR the ChatRoom is locked
     */
    @Override
    public void subscribe(IUser user) throws PermissionRequiredException {
        if(user instanceof User user1) {
            if(subscribedUsers.contains(user1)) return;
            if(roomIsLocked() && !user1.hasPermission("jonkcore.chatroom." + getIdentifier().toString().replaceAll(" ", "-") + ".bypasslock")) {
                throw new PermissionRequiredException(user1, "jonkcore.chatroom." + getIdentifier().toString().replaceAll(" ", "-") + ".bypasslock", "Unable to subscribe to chat room " + getIdentifier().toString() + ": Chat room is locked.");
            }
            if(!user1.hasPermission("jonkcore.chatroom." + getIdentifier().toString().replaceAll(" ", "-"))) {
                throw new PermissionRequiredException(user1, "jonkcore.chatroom." + getIdentifier().toString().replaceAll(" ", "-"), "Unable to subscribe to chat room " + getIdentifier().toString() + ": User does not have permission.");
            }
            subscribedUsers.add(user1);
            user1.subscribeToChat(this);
            user1.sendMessage("&7[&a+&7] &bYou have been added to the chatroom &a(" + getIdentifier().getString() + ")&b.");
        } else throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + user.getClass().getName() + ")");
    }

    /**
     * Unsubscribe a User from this ChatRoom.
     * <br><br>
     * Upon successfully leaving a ChatRoom, the User will be notified of their removal and the objects should be updated to reflect the change.
     *
     * @param user User to unsubscribe
     */
    @Override
    public void unsubscribe(IUser user) {
        if(user instanceof User user1) {
            try {
                if(!subscribedUsers.contains(user1)) return;
                subscribedUsers.remove(user1);
                user1.unsubscribeFromChat(this);
                user1.sendMessage("&7[&c-&7] &bYou have been removed from the chatroom &a(" + getIdentifier().getString() + ")&b.");
            } catch (PermissionRequiredException e) {
                user1.sendMessage("&cUnable to unsubscribe from chat room " + getIdentifier().toString() + ": User does not have permission.");
            }
        } else throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + user.getClass().getName() + ")");
    }

    /**
     * Check whether a User is subscribed to this ChatRoom.
     * @param user User to check
     * @return True if subscribed, false if not
     */
    @Override
    public boolean isSubscribed(IUser user) {
        if(user instanceof User user1) {
            return subscribedUsers.contains(user1);
        } else throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + user.getClass().getName() + ")");
    }

    /**
     * Lock this ChatRoom.
     * <br><br>
     * A locked ChatRoom will not allow any new Users to subscribe to it.
     * Existing Users will not be affected by this change, though they will be notified of the lock.
     */
    @Override
    public void lockRoom() {
        this.roomLock = true;
        sendMessage("&7[&4!&7] &cThis ChatRoom has been locked by an administrator. No new users can join.");
    }

    /**
     * Unlock this ChatRoom.
     * <br><br>
     * Reverses the effects of {@link #lockRoom()}.
     */
    @Override
    public void unlockRoom() {
        this.roomLock = false;
        sendMessage("&7[&4!&7] &aThis ChatRoom has been unlocked by an administrator. New users can now join.");
    }

    /**
     * Check whether this ChatRoom is locked.
     * <p>
     * A locked ChatRoom will not allow any new Users to subscribe to it.
     * Previously subscribed Users will also be unable to chat in the ChatRoom unless they have permission to do so.
     *
     * @return True if locked, false if not
     */
    @Override
    public boolean roomIsLocked() {
        return roomLock;
    }

    /**
     * Mute this ChatRoom.
     * <br><br>
     * A muted ChatRoom will not allow any Users to send messages, <b>except</b> those with a <code>pluginname.chat.chatname.bypassmute</code> permission
     * (this is a generalised permission - the User may also have Operator status, a generic chat permission such as <code>pluginname.chat.*.bypassmute</code>,
     * or a wildcard permission).
     */
    @Override
    public void muteRoom() {
        this.roomMute = true;
        sendMessage("&7[&4!&7] &cThis ChatRoom has been muted by an administrator. &7(requires &4pluginname.chat." + getIdentifier().toString().replaceAll(" ", "-") + ".bypassmute&7 permission to bypass)");
    }

    /**
     * Unmute this ChatRoom.
     * <br><br>
     * Reverses the effects of {@link #muteRoom()}.
     */
    @Override
    public void unmuteRoom() {
        this.roomMute = false;
        sendMessage("&7[&4!&7] &aThis ChatRoom has been unmuted by an administrator.");
    }

    /**
     * Check whether this ChatRoom is muted.
     * <br><br>
     * A muted ChatRoom will not allow any Users to send messages, <b>except</b> those with a <code>pluginname.chat.chatname.bypassmute</code> permission
     * (this is a generalised permission - the User may also have Operator status, a generic chat permission such as <code>pluginname.chat.bypassmute</code>,
     * or a wildcard permission).
     *
     * @return True if muted, false if not
     */
    @Override
    public boolean roomIsMuted() {
        return roomMute;
    }

    /**
     * Find all applicable ChatRooms.
     * <br><br>
     * This method will return all (persistent) ChatRooms that a Player has permission to join.
     * @param p Player to check
     * @return List of ChatRooms
     */
    public static ArrayList<ChatRoom> findApplicableRooms(Player p) {
        ArrayList<ChatRoom> applicableRooms = new ArrayList<>();
        for(IChatRoom room : JonkCORE.getInstance().getStorage().getChatRooms()) {
            if(!(room instanceof ChatRoom room1)) continue;
            if(!(room1.isLocked())) continue;
            if(room1.getPermission() == null || room1.getPermission().isEmpty()) {
                applicableRooms.add(room1);
                continue;
            }
            if(PermissionUtils.legacyCheckAny(p, room1.getPermission())) {
                applicableRooms.add(room1);
            }
        }
        return applicableRooms;
    }

    /**
     * Find all applicable ChatRooms.
     * <br><br>
     * This method will return all (persistent) ChatRooms that a User has permission to join.
     * @param u User to check
     * @return List of ChatRooms
     */
    public static ArrayList<ChatRoom> findApplicableRooms(User u) {
        ArrayList<ChatRoom> applicableRooms = new ArrayList<>();
        for(IChatRoom room : JonkCORE.getInstance().getStorage().getChatRooms()) {
            if(!(room instanceof ChatRoom room1)) continue;
            if(!(room1.isLocked())) continue;
            if(room1.getPermission() == null || room1.getPermission().isEmpty()) {
                applicableRooms.add(room1);
                continue;
            }
            if(u.hasPermission(room1.getPermission())) {
                applicableRooms.add(room1);
            }
        }
        return applicableRooms;
    }
}
