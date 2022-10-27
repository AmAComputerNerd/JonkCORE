package dev.simplyamazing.jonkcore.Objects;

import dev.simplyamazing.jonkcore.Exceptions.PermissionRequiredException;
import dev.simplyamazing.jonkcore.Exceptions.UserException;
import dev.simplyamazing.jonkcore.JonkCORE;
import dev.simplyamazing.jonkcore.Objects.Interfaces.CrossPluginObject;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IChatRoom;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IJonkPlugin;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import dev.simplyamazing.jonkcore.Utilities.ChatUtilities;
import dev.simplyamazing.jonkcore.Utilities.PermissionUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class User extends PluginObject implements IUser {
    // server target variables
    @Nullable protected CommandSender legacySender;
    @Nullable protected Player legacyPlayer;
    // chatroom variables
    protected ChatRoom focusedChatRoom;
    protected List<ChatRoom> subscribedChatRooms;
    // attribute variable
    protected List<Attribute> attributes;

    /**
     * Constructor for a User object with a legacy CommandSender.
     * @param legacySender The legacy CommandSender to create the User object from.
     */
    protected User(final CommandSender legacySender) {
        super(new ID(legacySender.getName()));
        this.legacySender = legacySender;
        this.legacyPlayer = null;
        this.focusedChatRoom = JonkCORE.getInstance().getStorage().getChatRoom("global");
        this.subscribedChatRooms = new ArrayList<>(Collections.singletonList(JonkCORE.getInstance().getStorage().getChatRoom("global")));
        this.attributes = new ArrayList<>();
        try {
            focusedChatRoom.subscribe(this);
        } catch(PermissionRequiredException ignored) {}
    }

    /**
     * Constructor for a User object with a legacy Player.
     * @param legacyPlayer The legacy Player to create the User object from.
     */
    protected User(final Player legacyPlayer) {
        super(new ID(legacyPlayer.getUniqueId()));
        this.legacyPlayer = legacyPlayer;
        this.legacySender = null;
        this.focusedChatRoom = JonkCORE.getInstance().getStorage().getChatRoom("global");
        this.subscribedChatRooms = new ArrayList<>(ChatRoom.findApplicableRooms(legacyPlayer));
        this.attributes = new ArrayList<>();
        try {
            focusedChatRoom.subscribe(this);
        } catch(PermissionRequiredException ignored) {}
    }

    /**
     * Retrieve an associated Bukkit Player object, if this User is a Player.
     * <br><br>
     * The Player object is cached, so this method is safe to call repeatedly.
     * Before running operations on the return value, null checks should be performed to ensure this object does not throw an un-caught {@link NullPointerException}.
     *
     * @return The associated Bukkit Player object, or null if this User is not a Player.
     */
    @Override
    public Player unsafeGetPlayer() {
        return legacyPlayer;
    }

    /**
     * Retrieve an associated Bukkit CommandSender object.
     * <br><br>
     * The CommandSender object is cached, so this method is safe to call repeatedly.
     * This method will never return null - always an instance of CommandSender.
     *
     * @return The associated Bukkit CommandSender object.
     */
    @Override
    public CommandSender safeGetLegacy() {
        return (legacyPlayer == null) ? legacySender : legacyPlayer;
    }

    /**
     * Check if this User is a Player.
     *
     * @return True if this User is a Player, false otherwise.
     */
    @Override
    public boolean isPlayer() {
        return legacySender == null;
    }

    /**
     * Retrieve the ChatRoom this User is currently focused on.
     * <br><br>
     * By default, the focused ChatRoom of a User is where all messages are routed to.
     * This can be changed by using the {@link #setFocusedChatRoom(IChatRoom)} method.
     * The default value of the focused ChatRoom is the <code>global</code> ChatRoom, which should always be generated on the plugin being enabled.
     *
     * @return The ChatRoom this User is currently focused on.
     */
    @Override
    public ChatRoom getFocusedChatRoom() {
        return focusedChatRoom;
    }

    /**
     * Retrieve the list of ChatRooms this User is currently in.
     *
     * @return The list of ChatRooms this User is currently in.
     */
    @Override
    public List<IChatRoom> getChatRooms() {
        return new ArrayList<>(subscribedChatRooms);
    }

    /**
     * Subscribe a User to a ChatRoom.
     * <br><br>
     * This method is a shortcut for {@link ChatRoom#subscribe(IUser)}.
     * The ChatRoom will be added to the list of ChatRooms this User is currently in, thus enabling them to use the `/chat` command or other methods to switch to this chat.
     * <br><br>
     * A {@link PermissionRequiredException} will be thrown if the User does not contain permission to join the ChatRoom.
     * An {@link IllegalArgumentException} will be thrown if the provided ChatRoom is not an instance of {@link ChatRoom}.
     * <br><br>
     * This method is also safe to call repeatedly, as it will not add the User to the ChatRoom if they are already subscribed.
     *
     * @param chatRoom The ChatRoom to subscribe this User to.
     * @throws PermissionRequiredException If the User does not contain permission to join the ChatRoom.
     */
    @Override
    public void subscribeToChat(IChatRoom chatRoom) throws PermissionRequiredException {
        if(chatRoom == null) return;
        if(chatRoom instanceof ChatRoom chatRoom1) {
            if(!this.subscribedChatRooms.contains(chatRoom1)) {
                if(chatRoom1.isSubscribed(this)) {
                    subscribedChatRooms.add(chatRoom1);
                } else {
                    chatRoom1.subscribe(this);
                }
            }
        } else throw new IllegalArgumentException("Provided ChatRoom is a differing implementation than required (Expected: " + ChatRoom.class.getName() + ", Provided: " + chatRoom.getClass().getName() + ")");
    }

    /**
     * Unsubscribe a User from a ChatRoom.
     * <br><br>
     * This method is a shortcut for {@link ChatRoom#unsubscribe(IUser)}.
     * <br><br>
     * A {@link PermissionRequiredException} will be thrown if the User is attempting to leave a special ChatRoom, such as the <code>global</code>> ChatRoom.
     * An {@link IllegalArgumentException} will be thrown if the provided ChatRoom is not an instance of {@link ChatRoom}.
     *
     * @param chatRoom The ChatRoom to unsubscribe this User from.
     * @throws PermissionRequiredException If the User is attempting to leave a special ChatRoom.
     */
    @Override
    public void unsubscribeFromChat(IChatRoom chatRoom) throws PermissionRequiredException {
        if(chatRoom == null) return;
        if(chatRoom instanceof ChatRoom chatRoom1) {
            if(chatRoom1.getIdentifier().toString().equals("global")) throw new PermissionRequiredException(this, "", "Cannot leave the global chat room");
            if(subscribedChatRooms.contains(chatRoom1)) {
                if(chatRoom1.isSubscribed(this)) {
                    chatRoom1.unsubscribe(this);
                } else {
                    subscribedChatRooms.remove(chatRoom1);
                }
                if(focusedChatRoom.equals(chatRoom1)) {
                    focusedChatRoom = JonkCORE.getInstance().getStorage().getChatRoom("global");
                    sendMessage(JonkCORE.getInstance().getPrefix() + "&cYou have been unsubscribed from the chat room &e" + chatRoom1.getIdentifier().toString() + "&c, so you have been moved to the global chat room.");
                }
            }
        } else throw new IllegalArgumentException("Provided ChatRoom is a differing implementation than required (Expected: " + ChatRoom.class.getName() + ", Provided: " + chatRoom.getClass().getName() + ")");
    }

    /**
     * Set the ChatRoom this User is currently focused on.
     * <br><br>
     * By default, the focused ChatRoom of a User is where all messages are routed to.
     * <br><br>
     * A {@link UserException} will be thrown if the User is not subscribed to the ChatRoom they are attempting to focus on, or another error occurs.
     * An {@link IllegalArgumentException} will be thrown if the provided ChatRoom is not an instance of {@link ChatRoom}.
     *
     * @param chatRoom The ChatRoom to focus on.
     * @throws UserException If the User is not subscribed to the ChatRoom they are attempting to focus on, or another error occurs.
     */
    @Override
    public void setFocusedChatRoom(IChatRoom chatRoom) throws UserException {
        if(chatRoom == null) return;
        if(chatRoom instanceof ChatRoom chatRoom1) {
            if(subscribedChatRooms.contains(chatRoom1)) {
                if(chatRoom1.isSubscribed(this)) {
                    focusedChatRoom = chatRoom1;
                } else throw new UserException(this, "User cannot focus on a ChatRoom they are not subscribed to.");
            } else throw new UserException(this, "User cannot focus on a ChatRoom they are not subscribed to.");
        } else throw new IllegalArgumentException("Provided ChatRoom is a differing implementation than required (Expected: " + ChatRoom.class.getName() + ", Provided: " + chatRoom.getClass().getName() + ")");
    }

    /**
     * Check if a User is subscribed to a ChatRoom.
     *
     * @param chatRoom The ChatRoom to check if the User is subscribed to.
     * @return True if the User is subscribed to the ChatRoom, false otherwise.
     */
    @Override
    public boolean hasChatRoom(IChatRoom chatRoom) {
        if(chatRoom == null) return false;
        if(chatRoom instanceof ChatRoom chatRoom1) {
            return subscribedChatRooms.contains(chatRoom1);
        } else throw new IllegalArgumentException("Provided ChatRoom is a differing implementation than required (Expected: " + ChatRoom.class.getName() + ", Provided: " + chatRoom.getClass().getName() + ")");
    }

    /**
     * Check if a User is subscribed to a ChatRoom.
     *
     * @param chatRoom The ChatRoom to check if the User is subscribed to.
     * @return True if the User is subscribed to the ChatRoom, false otherwise.
     */
    @Override
    public boolean hasChatRoom(ID chatRoom) {
        if(chatRoom == null) return false;
        return subscribedChatRooms.stream().anyMatch(chatRoom1 -> chatRoom1.getIdentifier().equals(chatRoom));
    }

    /**
     * Send a message to this User.
     * <br><br>
     * The message will be automatically formatted, and sent privately to the User.
     *
     * @param msg The message to send.
     */
    @Override
    public void sendMessage(String msg) {
        if(msg == null) return;
        if(msg.isEmpty()) return;
        safeGetLegacy().sendMessage(ChatUtilities.autoColour(msg));
    }

    /**
     * Mass-send some messages to this User.
     * <br><br>
     * The messages will be automatically formatted, and sent privately to the User.
     *
     * @param msg The messages to send.
     */
    @Override
    public void sendMessages(String... msg) {
        Arrays.stream(msg).forEach(this::sendMessage);
    }

    /**
     * Mass-send some messages to this User.
     * <br><br>
     * The messages will be automatically formatted, and sent privately to the User.
     *
     * @param msg The messages to send.
     */
    @Override
    public void sendMessages(List<String> msg) {
        msg.forEach(this::sendMessage);
    }

    /**
     * Send a message as the User to their focused ChatRoom.
     * <br><br>
     * This method is a shortcut for {@link ChatRoom#sendMessage(IUser, String)} within the focused room.
     * <br><br>
     * The message will be automatically formatted in accordance with the ChatRoom's associated ChatStyling.
     *
     * @param msg The message to send.
     */
    @Override
    public void sudoSendMessage(String msg) {
        if(msg == null) return;
        if(msg.isEmpty()) return;
        if(getFocusedChatRoom() == null) return;
        getFocusedChatRoom().sendMessage(this, msg);
    }

    /**
     * Mass-send a message as the User to their focused ChatRoom.
     * <br><br>
     * The messages will be automatically formatted in accordance with the ChatRoom's associated ChatStyling.
     *
     * @param msg The messages to send.
     */
    @Override
    public void sudoSendMessages(String... msg) {
        Arrays.stream(msg).forEach(this::sudoSendMessage);
    }

    /**
     * Mass-send a message as the User to their focused ChatRoom.
     * <br><br>
     * The messages will be automatically formatted in accordance with the ChatRoom's associated ChatStyling.
     *
     * @param msg The messages to send.
     */
    @Override
    public void sudoSendMessages(List<String> msg) {
        msg.forEach(this::sudoSendMessage);
    }

    /**
     * Send a message as the User to a specified ChatRoom.
     * <br><br>
     * This method is a shortcut for {@link ChatRoom#sendMessage(IUser, String)}.
     * <br><br>
     * The message will be automatically formatted in accordance with the ChatRoom's associated ChatStyling.
     * <br><br>
     * A {@link UserException} will be thrown if the User is not subscribed to the ChatRoom.
     * An {@link IllegalArgumentException} will be thrown if the provided ChatRoom is not an instance of {@link ChatRoom}.
     *
     * @param chatRoom The ChatRoom to send the message to.
     * @param msg      The message to send.
     * @throws UserException If the User is not subscribed to the ChatRoom they are attempting to send a message to.
     */
    @Override
    public void sudoSendMessage(IChatRoom chatRoom, String msg) throws UserException {
        if(chatRoom == null) return;
        if(msg == null) return;
        if(msg.isEmpty()) return;
        if(chatRoom instanceof ChatRoom chatRoom1) {
            if(!chatRoom1.isSubscribed(this)) throw new UserException(this, "User is not subscribed to ChatRoom. {chatRoom=" + chatRoom1.getIdentifier().toString() + "}");
            chatRoom1.sendMessage(this, msg);
        } else throw new IllegalArgumentException("Provided ChatRoom is a differing implementation than required (Expected: " + ChatRoom.class.getName() + ", Provided: " + chatRoom.getClass().getName() + ")");
    }

    /**
     * Mass-send a message as the User to a specified ChatRoom.
     * <br><br>
     * The messages will be automatically formatted in accordance with the ChatRoom's associated ChatStyling.
     * <br><br>
     * A {@link UserException} will be thrown if the User is not subscribed to the ChatRoom.
     * An {@link IllegalArgumentException} will be thrown if the provided ChatRoom is not an instance of {@link ChatRoom}.
     *
     * @param chatRoom The ChatRoom to send the messages to.
     * @param msg      The messages to send.
     * @throws UserException If the User is not subscribed to the ChatRoom they are attempting to send a message to.
     */
    @Override
    public void sudoSendMessages(IChatRoom chatRoom, String... msg) throws UserException {
        for(String message : msg) sudoSendMessage(chatRoom, message);
    }

    /**
     * Mass-send a message as the User to a specified ChatRoom.
     * <br><br>
     * The messages will be automatically formatted in accordance with the ChatRoom's associated ChatStyling.
     * <br><br>
     * A {@link UserException} will be thrown if the User is not subscribed to the ChatRoom.
     * An {@link IllegalArgumentException} will be thrown if the provided ChatRoom is not an instance of {@link ChatRoom}.
     *
     * @param chatRoom The ChatRoom to send the messages to.
     * @param msg      The messages to send.
     * @throws UserException If the User is not subscribed to the ChatRoom they are attempting to send a message to.
     */
    @Override
    public void sudoSendMessages(IChatRoom chatRoom, List<String> msg) throws UserException {
        for(String message : msg) sudoSendMessage(chatRoom, message);
    }

    /**
     * Check if this User has a particular Permission.
     * <br><br>
     * This method is a shortcut for {@link PermissionUtils#checkAny(IUser, String)}.
     *
     * @param permission The Permission to check for.
     * @return True if this User has the Permission, false otherwise.
     */
    @Override
    public boolean hasPermission(String permission) {
        return PermissionUtils.checkAny(this, permission);
    }

    /**
     * Get the sub-plugin that this object belongs to.
     * <br><br>
     * The returned value will be a server-side implementation of the plugin.
     *
     * @return The sub-plugin that this object belongs to.
     */
    @Override
    public IJonkPlugin getOriginPlugin() {
        return JonkCORE.getInstance();
    }

    /**
     * Get the name of the object that this object is.
     *
     * @return The name of the object.
     */
    @Override
    public String getObjectName() {
        return getClass().getSimpleName();
    }

    /**
     * Pack this object, resulting in a generic object that can be used by other sub-plugins.
     * <br><br>
     * All variables that are not implemented will be stored as Attributes within the object.
     *
     * @return The packed object.
     */
    @Override
    public IUser pack() {
        return this;
    }

    /**
     * Unpack this object, resulting in a sub-plugin specific object.
     * <br><br>
     * All variables that are not implemented will be stored as Attributes within the object.
     * Upon unpacking, any Attributes that match a variable within that object will be set to that variable and deleted.
     *
     * @param packed The packed object to unpack.
     */
    @Override
    public void unpack(CrossPluginObject packed) {
        if(!(packed instanceof IUser user)) return;
        this.legacySender = (user.isPlayer()) ? null : user.safeGetLegacy();
        this.legacyPlayer = (user.isPlayer()) ? user.unsafeGetPlayer() : null;
        this.subscribedChatRooms.clear();
        user.getChatRooms().forEach(chatRoom -> {
            if(chatRoom instanceof ChatRoom chatRoom1) {
                try {
                    chatRoom1.subscribe(this);
                } catch(PermissionRequiredException ignored) {
                    JonkCORE.getInstance().getLogger().warning("Failed to subscribe converted User to ChatRoom. {user=" + user + ", chatRoom=" + chatRoom1.getIdentifier().toString() + ", plugin=" + user.getOriginPlugin().getPrefix() + "}");
                }
            } else JonkCORE.getInstance().getLogger().warning("Couldn't subscribe converted User to ChatRoom. {user=" + user + ", chatRoom=" + chatRoom.toString() + ", plugin=" + user.getOriginPlugin().getPrefix() + "}");
        });
        this.attributes.clear();
        user.getAttributes().forEach(this::addAttribute);
        this.focusedChatRoom = (user.getFocusedChatRoom() != null && user.getFocusedChatRoom() instanceof ChatRoom) ? (ChatRoom)user.getFocusedChatRoom() : focusedChatRoom;
    }

    /**
     * Retrieve the list of Attributes this User has.
     * <br><br>
     * Attributes are used to store data about foreign plugin object variables.
     * <br><br>
     * Attributes follow strict rules when being stored:
     * <ul>
     *     <li>An Attribute cannot be stored in file. Any Attributes that an object contains when being stored are ignored.</li>
     *     <li>Each stored Attribute represents a variable within a foreign object.</li>
     *     <li>Attributes <i>can</i> be changed, either in name or value, but this can result in malformed information.</li>
     * </ul>
     *
     * @return The list of Attributes this CrossPluginObject has.
     */
    @Override
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Retrieve an Attribute this User has based on an ID.
     * <br><br>
     * The ID is used to identify the Attribute, and is normally generated based on the plugin name and variable name separated by a colon.
     * (e.g. <code>myplugin:myvariable</code>)
     *
     * @param id The ID of the Attribute to retrieve.
     * @return The Attribute this User has based on an ID.
     */
    @Override
    public Attribute getAttribute(ID id) {
        return attributes.stream().filter(attribute -> attribute.getIdentifier().equals(id)).findFirst().orElse(null);
    }

    /**
     * Add an Attribute to this User.
     * <br><br>
     * Attributes are temporary variables that are used to store data about foreign plugin object variables.
     * <br><br>
     * Assigning an Attribute to an object should not be done manually, as it can result in malformed information.
     * Instead, it is recommended to use the {@link #pack()} method to pack the object, and then unpack it using the {@link #unpack(CrossPluginObject)} method.
     * <br><br>
     * Attributes follow strict rules when being stored:
     * <ul>
     *     <li>An Attribute cannot be stored in file. Any Attributes that an object contains when being stored are ignored.</li>
     *     <li>Each stored Attribute represents a variable within a foreign object.</li>
     *     <li>Attributes <i>can</i> be changed, either in name or value, but this can result in malformed information.</li>
     * </ul>
     *
     * @param attribute The Attribute to add to this User.
     */
    @Override
    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    /**
     * Remove an Attribute from this User using its ID.
     * <br><br>
     * Attributes are temporary variables that are used to store data about foreign plugin object variables.
     * <br><br>
     * Removing an Attribute may result in data loss between conversions.
     * It is recommended to use the {@link #unpack(CrossPluginObject)} method to remove Attributes.
     * <br><br>
     * Attributes follow strict rules when being stored:
     * <ul>
     *     <li>An Attribute cannot be stored in file. Any Attributes that an object contains when being stored are ignored.</li>
     *     <li>Each stored Attribute represents a variable within a foreign object.</li>
     *     <li>Attributes <i>can</i> be changed, either in name or value, but this can result in malformed information.</li>
     *     <li>Attributes <i>can</i> be removed, but this can result in data loss between conversions.</li>
     * </ul>
     *
     * @param id The ID of the Attribute to remove.
     */
    @Override
    public void removeAttribute(ID id) {
        attributes.removeIf(attribute -> attribute.getIdentifier().equals(id));
    }

    /**
     * Remove an Attribute from this User.
     * <br><br>
     * Attributes are temporary variables that are used to store data about foreign plugin object variables.
     * <br><br>
     * Removing an Attribute may result in data loss between conversions.
     * It is recommended to use the {@link #unpack(CrossPluginObject)} method to remove Attributes.
     * <br><br>
     * Attributes follow strict rules when being stored:
     * <ul>
     *     <li>An Attribute cannot be stored in file. Any Attributes that an object contains when being stored are ignored.</li>
     *     <li>Each stored Attribute represents a variable within a foreign object.</li>
     *     <li>Attributes <i>can</i> be changed, either in name or value, but this can result in malformed information.</li>
     *     <li>Attributes <i>can</i> be removed, but this can result in data loss between conversions.</li>
     * </ul>
     *
     * @param attribute The Attribute to remove.
     */
    @Override
    public void removeAttribute(Attribute attribute) {
        attributes.remove(attribute);
    }

    /**
     * Check if this User has a particular Attribute using its ID.
     *
     * @param id The ID of the Attribute to check for.
     * @return True if this User has the Attribute, false otherwise.
     */
    @Override
    public boolean hasAttribute(ID id) {
        return attributes.stream().anyMatch(attribute -> attribute.getIdentifier().equals(id));
    }

    /**
     * Check if this User has a particular Attribute.
     *
     * @param attribute The Attribute to check for.
     * @return True if this User has the Attribute, false otherwise.
     */
    @Override
    public boolean hasAttribute(Attribute attribute) {
        return attributes.contains(attribute);
    }

    /**
     * Fetch a User object from a legacy CommandSender.
     * <br><br>
     * The {@link CommandSender} will be automatically checked on whether it's an instance of Player, and if so, will either return a new User object or an existing one from the {@link Index}.
     * If the {@link CommandSender} is not an instance of Player, a new User object will be created.
     * <br><br>
     * Any returned User object is <b>not</b> added to the {@link Index}, even if the object is newly created. This should be done manually as usage varies.
     *
     * @param sender The CommandSender object.
     * @return The User object.
     */
    public static User fetch(CommandSender sender) {
        if(sender instanceof Player player) {
            if(JonkCORE.getInstance().getStorage().getUser(new ID(player.getUniqueId())) == null) return new User((Player)sender);
            return JonkCORE.getInstance().getStorage().getUser(new ID(player.getUniqueId()));
        }
        return new User(sender);
    }

    /**
     * Fetch an empty User object for limited use cases where a User object is required.
     * <br><br>
     * In technicality, the returned User object is not empty, but rather a User object with target CONSOLE.
     * <br><br>
     * This object should <b>not</b> be used for any other purpose than to satisfy a method that requires a User object.
     *
     * @return The empty User object.
     */
    public static User fetchEmpty() {
        return new User(JonkCORE.getInstance().getServer().getConsoleSender());
    }

    /**
     * Fetch a copy of a player-User object.
     * <br><br>
     * This method is used to create a copy of a player-User object without fetching it from the {@link Index}.
     * This is useful for modifying a User object without affecting the original.
     *
     * @param p The player to copy.
     * @return The copied User object.
     */
    public static User fetchCopy(Player p) {
        return new User(p);
    }
}
