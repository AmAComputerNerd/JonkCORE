package dev.simplyamazing.jonkcore.Objects;

import dev.simplyamazing.jonkcore.Objects.Command.PluginCommand;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IChatRoom;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IStorage;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Index implements IStorage {
    private final List<ChatRoom> chatRooms;
    private final List<User> users;

    /**
     * Constructor for the Index object.
     */
    public Index() {
        this.chatRooms = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    /**
     * Retrieve the list of all users.
     * @return list of all users
     */
    @Override
    public List<IUser> getUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Retrieve a User object from their name.
     * @param name name of the user
     * @return User object of the user
     */
    @Override
    public User getUser(String name) {
        for(User u : users) {
            if(u.isPlayer()) {
                if(u.unsafeGetPlayer().getName().equalsIgnoreCase(name)) {
                    return u;
                }
            }
            if(u.getIdentifier().getIdentifier().equals(name)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Retrieve a User object from their ID.
     * @param id ID of the user
     * @return User object of the user
     */
    @Override
    public User getUser(ID id) {
        for(User u : users) {
            if(u.getIdentifier().equals(id)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Register a new User object.
     * @param user User object to register
     */
    @Override
    public void registerUser(IUser user) {
        if(user instanceof User user1) {
            if(!users.contains(user1)) {
                this.users.add(user1);
            }
        } else throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + user.getClass().getName() + ")");
    }

    /**
     * Register a new User object from a Player object.
     * @param legacy legacy player object
     */
    @Override
    public void registerUser(Player legacy) {
        if(getUser(legacy.getUniqueId().toString()) == null) {
            this.users.add(User.fetch(legacy));
        }
    }

    /**
     * Unregister a User object.
     * @param user User object to unregister
     */
    @Override
    public void unregisterUser(IUser user) {
        if(user instanceof User user1) {
            this.users.remove(user1);
        } else throw new IllegalArgumentException("Provided User is a differing implementation than required (Expected: " + User.class.getName() + ", Provided: " + user.getClass().getName() + ")");
    }

    /**
     * Unregister a User object from their ID.
     * @param id ID of the user
     */
    @Override
    public void unregisterUser(ID id) {
        for(int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if(u.getIdentifier().equals(id)) {
                this.users.remove(u);
                return;
            }
        }
    }

    /**
     * Clear all registered User objects.
     * <br><br>
     * This should, ideally, only be used when the plugin is being disabled.
     * Removing a player's associated User object can and likely will break things, so ensure you know what you're doing.
     */
    @Override
    public void clearUsers() {
        this.users.clear();
    }

    /**
     * Retrieve the list of all chat rooms.
     * @return list of all chat rooms
     */
    @Override
    public List<IChatRoom> getChatRooms() {
        return new ArrayList<>(chatRooms);
    }

    /**
     * Retrieve a ChatRoom object from its name.
     * @param name name of the chat room
     * @return retrieved ChatRoom object, or a null-equivalent if the chat room does not exist
     */
    @Override
    public ChatRoom getChatRoom(String name) {
        for(ChatRoom c : chatRooms) {
            if(c.getIdentifier().toString().equals(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Retrieve a ChatRoom object from its ID.
     * @param id ID of the chat room
     * @return retrieved ChatRoom object, or a null-equivalent if the chat room does not exist
     */
    @Override
    public ChatRoom getChatRoom(ID id) {
        for(ChatRoom c : chatRooms) {
            if(c.getIdentifier().equals(id)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Register a new ChatRoom object.
     * @param chatRoom ChatRoom object to register
     */
    @Override
    public void registerChatRoom(IChatRoom chatRoom) {
        if(chatRoom instanceof ChatRoom chatRoom1) {
            if(!chatRooms.contains(chatRoom1)) {
                this.chatRooms.add(chatRoom1);
            }
        } else throw new IllegalArgumentException("Provided ChatRoom is a differing implementation than required (Expected: " + ChatRoom.class.getName() + ", Provided: " + chatRoom.getClass().getName() + ")");
    }

    /**
     * Unregister a ChatRoom object.
     * @param chatRoom ChatRoom object to unregister
     */
    @Override
    public void unregisterChatRoom(IChatRoom chatRoom) {
        if(chatRoom instanceof ChatRoom chatRoom1) {
            this.chatRooms.remove(chatRoom1);
        } else throw new IllegalArgumentException("Provided ChatRoom is a differing implementation than required (Expected: " + ChatRoom.class.getName() + ", Provided: " + chatRoom.getClass().getName() + ")");
    }

    /**
     * Unregister a ChatRoom object from its ID.
     * @param id ID of the chat room
     */
    @Override
    public void unregisterChatRoom(ID id) {
        for(int i = 0; i < chatRooms.size(); i++) {
            ChatRoom c = chatRooms.get(i);
            if(c.getIdentifier().equals(id)) {
                this.chatRooms.remove(c);
                return;
            }
        }
    }

    /**
     * Clear all registered ChatRoom objects.
     * <br><br>
     * This should, ideally, only be used when the plugin is being disabled.
     * Removing all ChatRoom objects will undoubtedly break things, so ensure you know what you're doing.
     */
    @Override
    public void clearChatRooms() {
        this.chatRooms.clear();
    }
}
