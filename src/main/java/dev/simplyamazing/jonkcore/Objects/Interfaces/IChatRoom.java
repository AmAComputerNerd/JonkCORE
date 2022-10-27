package dev.simplyamazing.jonkcore.Objects.Interfaces;

import dev.simplyamazing.jonkcore.Exceptions.PermissionRequiredException;

import java.util.List;

public interface IChatRoom {
    /**
     * Retrieve the chat styling for this ChatRoom.
     * <br><br>
     * The ChatStyling object contains all formatting and styling options for this ChatRoom.
     * <br>
     * This often includes but is not limited to a prefix, suffix, default colour and permission standards.
     *
     * @return ChatStyling object
     */
    IChatStyling getChatStyle();

    /**
     * Retrieve the trigger keyword that will automatically send a following message to this ChatRoom.
     * <br><br>
     * The keyword, which normally ends in a symbol character, is unique to a ChatRoom, and will result in any message sent with the prefixing keyword to be
     * redirected to this ChatRoom.
     * <br>
     * This will only happen if the User sending the message is subscribed to the ChatRoom and maintains permission to speak in it.
     *
     * @return String keyword
     */
    String getTriggerKeyword();

    /**
     * Retrieve the lowest permission required to subscribe to and speak in this ChatRoom.
     * <br><br>
     * The permission is checked upon subscribing to the room and each time before the User chats in the room.
     * If a User no longer has permission to chat and see the room, they will be automatically unsubscribed and have their focused room reset if it was set to this ChatRoom.
     *
     * @return String permission
     */
    String getPermission();

    /**
     * Retrieve the list of Users subscribed to this ChatRoom.
     * @return List of Users
     */
    List<IUser> getSubscribedUsers();

    /**
     * Send a message to all Users subscribed to this ChatRoom.
     * <br><br>
     * This message will be automatically formatted and styled according to the ChatStyling object of this ChatRoom.
     * <br><br>
     * The alternative {@link #sendMessage(IUser, String)} method should instead be used if trying to send a message from a specific User.
     *
     * @param message Message to send
     */
    void sendMessage(String message);

    /**
     * Send a message to all Users subscribed to this ChatRoom, prefixed with the User's name.
     * <br><br>
     * This message will be automatically formatted and styled according to the ChatStyling object of this ChatRoom.
     * <br><br>
     * The alternative {@link #sendMessage(String)} method should instead be used if trying to send a generic message.
     *
     * @param sender User sending the message
     * @param message Message to send
     */
    void sendMessage(IUser sender, String message);

    /**
     * Subscribe a User to this ChatRoom.
     * <br><br>
     * The User will be automatically checked for permission to subscribe to the ChatRoom, and a {@link dev.simplyamazing.jonkcore.Exceptions.PermissionRequiredException} will be thrown if the User does not pass this check.
     * Upon successfully joining a ChatRoom, the User will be notified of their subscription and the objects should be updated to reflect the change.
     *
     * @param user User to subscribe
     * @throws PermissionRequiredException If the User does not have permission to subscribe to the ChatRoom
     */
    void subscribe(IUser user) throws PermissionRequiredException;

    /**
     * Unsubscribe a User from this ChatRoom.
     * <br><br>
     * Upon successfully leaving a ChatRoom, the User will be notified of their unsubscription and the objects should be updated to reflect the change.
     *
     * @param user User to unsubscribe
     */
    void unsubscribe(IUser user);

    /**
     * Check whether a User is subscribed to this ChatRoom.
     * @param user User to check
     * @return True if subscribed, false if not
     */
    boolean isSubscribed(IUser user);

    /**
     * Clean up this ChatRoom prior to deletion.
     * <br><br>
     * This method should be called before the ChatRoom is unregistered and deleted.
     * By default, this method will unsubscribe all Users from the ChatRoom before locking itself to prevent new Users from joining.
     */
    default void cleanup() {
        for(IUser user : getSubscribedUsers()) {
            unsubscribe(user);
        }
        lockRoom();
    }

    /**
     * Lock this ChatRoom.
     * <br><br>
     * A locked ChatRoom will not allow any new Users to subscribe to it, <b>except</b> those with a <code>pluginname.chat.bypasslock</code> permission.
     */
    void lockRoom();

    /**
     * Unlock this ChatRoom.
     * <br><br>
     * Reverses the effects of {@link #lockRoom()}.
     */
    void unlockRoom();

    /**
     * Check whether this ChatRoom is locked.
     * <br><br>
     * A locked ChatRoom will not allow any new Users to subscribe to it, <b>except</b> those with a <code>pluginname.chat.bypasslock</code> permission.
     *
     * @return True if locked, false if not
     */
    boolean roomIsLocked();

    /**
     * Mute this ChatRoom.
     * <br><br>
     * A muted ChatRoom will not allow any Users to send messages, <b>except</b> those with a <code>pluginname.chat.chatname.bypassmute</code> permission
     * (this is a generalised permission - the User may also have Operator status, a generic chat permission such as <code>pluginname.chat.bypassmute</code>,
     * or a wildcard permission).
     */
    void muteRoom();

    /**
     * Unmute this ChatRoom.
     * <br><br>
     * Reverses the effects of {@link #muteRoom()}.
     */
    void unmuteRoom();

    /**
     * Check whether this ChatRoom is muted.
     * <br><br>
     * A muted ChatRoom will not allow any Users to send messages, <b>except</b> those with a <code>pluginname.chat.chatname.bypassmute</code> permission
     * (this is a generalised permission - the User may also have Operator status, a generic chat permission such as <code>pluginname.chat.bypassmute</code>,
     * or a wildcard permission).
     *
     * @return True if muted, false if not
     */
    boolean roomIsMuted();
}
