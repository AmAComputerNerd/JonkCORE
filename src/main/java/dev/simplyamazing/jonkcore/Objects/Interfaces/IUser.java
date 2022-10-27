package dev.simplyamazing.jonkcore.Objects.Interfaces;

import dev.simplyamazing.jonkcore.Exceptions.PermissionRequiredException;
import dev.simplyamazing.jonkcore.Exceptions.UserException;
import dev.simplyamazing.jonkcore.Objects.ID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public interface IUser extends CrossPluginObject {
    /**
     * Retrieve an associated Bukkit Player object, if this User is a Player.
     * <br><br>
     * The Player object is cached, so this method is safe to call repeatedly.
     * Before running operations on the return value, null checks should be performed to ensure this object does not throw an un-caught {@link NullPointerException}.
     * @return The associated Bukkit Player object, or null if this User is not a Player.
     */
    Player unsafeGetPlayer();

    /**
     * Retrieve an associated Bukkit CommandSender object.
     * <br><br>
     * The CommandSender object is cached, so this method is safe to call repeatedly.
     * This method will never return null - always an instance of CommandSender.
     * @return The associated Bukkit CommandSender object.
     */
    CommandSender safeGetLegacy();

    /**
     * Check if this User is a Player.
     * @return True if this User is a Player, false otherwise.
     */
    boolean isPlayer();

    // ChatRoom-related methods

    /**
     * Retrieve the ChatRoom this User is currently focused on.
     * <br><br>
     * By default, the focused ChatRoom of a User is where all messages are routed to.
     * This can be changed by using the {@link #setFocusedChatRoom(IChatRoom)} method.
     * The default value of the focused ChatRoom is the `global` ChatRoom, which should always be generated on the plugin being enabled.
     * @return The ChatRoom this User is currently focused on.
     */
    IChatRoom getFocusedChatRoom();

    /**
     * Retrieve the list of ChatRooms this User is currently in.
     * @return The list of ChatRooms this User is currently in.
     */
    List<IChatRoom> getChatRooms();

    /**
     * Subscribe a User to a ChatRoom.
     * <br><br>
     * This method is a shortcut for {@link IChatRoom#subscribe(IUser)}.
     * The ChatRoom will be added to the list of ChatRooms this User is currently in, thus enabling them to use the `/chat` command or other methods to switch to this chat.
     * <br><br>
     * If a User does not contain permission to join a ChatRoom, a {@link PermissionRequiredException} will be thrown.
     * <br><br>
     * This method is also safe to call repeatedly, as it will not add the User to the ChatRoom if they are already subscribed.
     *
     * @param chatRoom The ChatRoom to subscribe this User to.
     * @throws PermissionRequiredException If the User does not contain permission to join the ChatRoom.
     */
    void subscribeToChat(final IChatRoom chatRoom) throws PermissionRequiredException;

    /**
     * Unsubscribe a User from a ChatRoom.
     * <br><br>
     * This method is a shortcut for {@link IChatRoom#unsubscribe(IUser)}.
     * A {@link PermissionRequiredException} will be thrown if the User is attempting to leave a special ChatRoom, such as the `global` ChatRoom.
     * @param chatRoom The ChatRoom to unsubscribe this User from.
     * @throws PermissionRequiredException If the User is attempting to leave a special ChatRoom.
     */
    void unsubscribeFromChat(final IChatRoom chatRoom) throws PermissionRequiredException;

    /**
     * Set the ChatRoom this User is currently focused on.
     * <br><br>
     * By default, the focused ChatRoom of a User is where all messages are routed to.
     * A {@link UserException} will be thrown if the User is not subscribed to the ChatRoom they are attempting to focus on, or another error occurs.
     * @param chatRoom The ChatRoom to focus on.
     * @throws UserException If the User is not subscribed to the ChatRoom they are attempting to focus on, or another error occurs.
     */
    void setFocusedChatRoom(final IChatRoom chatRoom) throws UserException;

    /**
     * Check if a User is subscribed to a ChatRoom.
     * @param chatRoom The ChatRoom to check if the User is subscribed to.
     * @return True if the User is subscribed to the ChatRoom, false otherwise.
     */
    boolean hasChatRoom(final IChatRoom chatRoom);

    /**
     * Check if a User is subscribed to a ChatRoom.
     * @param chatRoom The ChatRoom to check if the User is subscribed to.
     * @return True if the User is subscribed to the ChatRoom, false otherwise.
     */
    boolean hasChatRoom(final ID chatRoom);

    // Message-related methods

    /**
     * Send a message to this User.
     * <br><br>
     * The message will be automatically formatted, and sent privately to the User.
     * @param msg The message to send.
     */
    void sendMessage(String msg);

    /**
     * Mass-send some messages to this User.
     * <br><br>
     * The messages will be automatically formatted, and sent privately to the User.
     * @param msg The messages to send.
     */
    void sendMessages(String... msg);

    /**
     * Mass-send some messages to this User.
     * <br><br>
     * The messages will be automatically formatted, and sent privately to the User.
     * @param msg The messages to send.
     */
    void sendMessages(List<String> msg);

    /**
     * Send a message as the User to their focused ChatRoom.
     * <br><br>
     * This method is a shortcut for {@link IChatRoom#sendMessage(IUser, String)}.
     * The message will be automatically formatted in accordance with the ChatRoom's associated ChatStyling.
     * @param msg The message to send.
     */
    void sudoSendMessage(String msg);

    /**
     * Mass-send a message as the User to their focused ChatRoom.
     * <br><br>
     * The messages will be automatically formatted in accordance with the ChatRoom's associated ChatStyling.
     * @param msg The messages to send.
     */
    void sudoSendMessages(String... msg);

    /**
     * Mass-send a message as the User to their focused ChatRoom.
     * <br><br>
     * The messages will be automatically formatted in accordance with the ChatRoom's associated ChatStyling.
     * @param msg The messages to send.
     */
    void sudoSendMessages(List<String> msg);

    /**
     * Send a message as the User to a specified ChatRoom.
     * <br><br>
     * This method is a shortcut for {@link IChatRoom#sendMessage(IUser, String)}.
     * The message will be automatically formatted in accordance with the ChatRoom's associated ChatStyling.
     * @param chatRoom The ChatRoom to send the message to.
     * @param msg The message to send.
     * @throws UserException If the User is not subscribed to the ChatRoom they are attempting to send a message to.
     */
    void sudoSendMessage(IChatRoom chatRoom, String msg) throws UserException;

    /**
     * Mass-send a message as the User to a specified ChatRoom.
     * <br><br>
     * The messages will be automatically formatted in accordance with the ChatRoom's associated ChatStyling.
     * @param chatRoom The ChatRoom to send the messages to.
     * @param msg The messages to send.
     * @throws UserException If the User is not subscribed to the ChatRoom they are attempting to send a message to.
     */
    void sudoSendMessages(IChatRoom chatRoom, String... msg) throws UserException;

    /**
     * Mass-send a message as the User to a specified ChatRoom.
     * <br><br>
     * The messages will be automatically formatted in accordance with the ChatRoom's associated ChatStyling.
     * @param chatRoom The ChatRoom to send the messages to.
     * @param msg The messages to send.
     * @throws UserException If the User is not subscribed to the ChatRoom they are attempting to send a message to.
     */
    void sudoSendMessages(IChatRoom chatRoom, List<String> msg) throws UserException;

    // Permission-related methods

    /**
     * Check if this User has a particular Permission.
     * <br><br>
     * This method is a shortcut for {@link dev.simplyamazing.jonkcore.Utilities.PermissionUtils#checkAny(IUser, String)}.
     * @param permission The Permission to check for.
     * @return True if this User has the Permission, false otherwise.
     */
    boolean hasPermission(String permission);
}
