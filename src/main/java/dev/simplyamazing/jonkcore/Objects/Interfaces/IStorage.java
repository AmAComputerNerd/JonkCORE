package dev.simplyamazing.jonkcore.Objects.Interfaces;

import dev.simplyamazing.jonkcore.Objects.ID;
import org.bukkit.entity.Player;

import java.util.List;

public interface IStorage {
    /**
     * Retrieve the list of all users.
     * <br><br>
     * This list is used to store all users of the sub-plugin, and is used internally by the sub-plugin to retrieve information about players.
     * Likewise, legacy Player and CommandSender objects should be converted to User objects using the UserConverter object for standardisation purposes.
     * <br><br>
     * In almost all cases, the User object should be retrieved from this list, rather than creating a new one (either through one of the many getter methods
     * within this file, or through the returned list directly).
     *
     * @return list of all users
     */
    List<IUser> getUsers();

    /**
     * Retrieve a User object from their name.
     * <br><br>
     * This method shares the same functionality as {@link IUserConverter#convert(String)}, but does not guarantee the returned User object is valid (not null).
     * <br>
     * The provided name is used to search through the list of all users, and if a match is found, the User object is returned.
     * The name is not case-sensitive.
     * This method will also call {@link #getUser(ID)} if all previous checks fail, otherwise it will return null.
     *
     * @param name name of the user
     * @return User object of the user
     */
    IUser getUser(String name);

    /**
     * Retrieve a User object from their ID.
     * <br><br>
     * The provided ID <b>is</b> case-sensitive, and will only return a User object if the ID matches exactly.
     *
     * @param id ID of the user
     * @return User object of the user
     */
    IUser getUser(ID id);

    /**
     * Register a new User object.
     * <br><br>
     * As the name suggests, this method is used to register a new User object into the list of all users.
     * <br><br>
     * This method is used internally by the sub-plugin, and should not be used by other plugins.
     *
     * @param user User object to register
     */
    void registerUser(IUser user);

    /**
     * Register a new User object from a Player object.
     * <br><br>
     * This method is <b>not recommended</b> for general usage, outside of registering event classes.
     * It will not check if the Player object is already registered, and will simply create a new User object from the Player object.
     * This can lead to issues with one/both User objects being out-of-date, and will likely cause issues with the sub-plugin's internal storage system.
     * <br><br>
     * This method is used internally by the sub-plugin, and should not be used by other plugins.
     *
     * @param legacy legacy player object
     */
    void registerUser(Player legacy);

    /**
     * Unregister a User object.
     * <br><br>
     * This method is <b>not recommended</b> for general usage, outside of registering event classes.
     * Removing a User object from the list of online users <b>while</b> a Player is still online can cause synchronisation issues, and cause other unexpected behaviour.
     * <b>Use at your own risk.</b>
     * <br><br>
     * This method is used internally by the sub-plugin, and should not be used by other plugins.
     *
     * @param user User object to unregister
     */
    void unregisterUser(IUser user);

    /**
     * Unregister a User object from their ID.
     * <br><br>
     * This method is <b>not recommended</b> for general usage, outside of registering event classes.
     * Removing a User object from the list of online users <b>while</b> a Player is still online can cause synchronisation issues, and cause other unexpected behaviour.
     * <b>Use at your own risk.</b>
     * <br><br>
     * This method is used internally by the sub-plugin, and should not be used by other plugins.
     *
     * @param id ID of the user
     */
    void unregisterUser(ID id);

    /**
     * Clear all registered User objects.
     * <br><br>
     * This should, ideally, only be used when the plugin is being disabled.
     * Removing a player's associated User object can and likely will break things, so ensure you know what you're doing.
     * <br><br>
     * This method is used internally by the sub-plugin, and should not be used by other plugins.
     */
    void clearUsers();

    /**
     * Retrieve the list of all chat rooms.
     * <br><br>
     * This list is used to store all chat rooms of the sub-plugin, and is used internally by the sub-plugin to retrieve information about chat rooms.
     *
     * @return list of all chat rooms
     */
    List<IChatRoom> getChatRooms();

    /**
     * Retrieve a ChatRoom object from its name.
     * <br><br>
     * Functionality-wise, this method works the exact same as {@link #getChatRoom(ID)}.
     * The provided name is used to search through the list of all chat rooms, and if a match is found, the ChatRoom object is returned.
     *
     * @param name name of the chat room
     * @return retrieved ChatRoom object, or a null-equivalent if the chat room does not exist
     */
    IChatRoom getChatRoom(String name);

    /**
     * Retrieve a ChatRoom object from its ID.
     * <br><br>
     * The provided ID <b>is</b> case-sensitive, and will only return a ChatRoom object if the ID matches exactly.
     *
     * @param id ID of the chat room
     * @return retrieved ChatRoom object, or a null-equivalent if the chat room does not exist
     */
    IChatRoom getChatRoom(ID id);

    /**
     * Register a new ChatRoom object.
     * <br><br>
     * As the name suggests, this method is used to register a new ChatRoom object into the list of all chat rooms.
     * <br><br>
     * This method <b>can</b> be used by other plugins, but should be used with caution.
     *
     * @param chatRoom ChatRoom object to register
     */
    void registerChatRoom(IChatRoom chatRoom);

    /**
     * Unregister a ChatRoom object.
     * <br><br>
     * Finds and removes the provided ChatRoom object from the list of registered chat rooms, also calling {@link IChatRoom#cleanup} prior to removal.
     * If the ChatRoom object is not found, this method will do nothing.
     * <br><br>
     * This method <b>can</b> be used by other plugins, but should be used with caution.
     *
     * @param chatRoom ChatRoom object to unregister
     */
    void unregisterChatRoom(IChatRoom chatRoom);

    /**
     * Unregister a ChatRoom object from its ID.
     * <br><br>
     * Finds and removes the ChatRoom object with the provided ID from the list of registered chat rooms, also calling {@link IChatRoom#cleanup} prior to removal.
     * If the ChatRoom object is not found, this method will do nothing.
     * <br><br>
     * This method <b>can</b> be used by other plugins, but should be used with caution.
     * @param id ID of the chat room
     */
    void unregisterChatRoom(ID id);

    /**
     * Clear all registered ChatRoom objects.
     * <br><br>
     * This should, ideally, only be used when the plugin is being disabled.
     * Removing all ChatRoom objects will undoubtedly break things, so ensure you know what you're doing.
     * <br><br>
     * This method <b>can</b> be used by other plugins, but should be used with caution.
     */
    void clearChatRooms();

    /**
     * Save objects to hard-storage (e.g. a database).
     * <br><br>
     * <b>DEFAULT:</b> Does nothing, and throws an {@link UnsupportedOperationException}.
     */
    default void save() {
        throw new UnsupportedOperationException("This method is not supported by this storage type.");
    }

    /**
     * Load objects from hard-storage (e.g. a database).
     * <br><br>
     * <b>DEFAULT:</b> Does nothing, and throws an {@link UnsupportedOperationException}.
     */
    default void load() {
        throw new UnsupportedOperationException("This method is not supported by this storage type.");
    }
}
