package dev.simplyamazing.jonkcore.Objects;

import dev.simplyamazing.jonkcore.Exceptions.ConversionException;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IUserConverter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GenericUserConverter implements IUserConverter {
    /**
     * Convert from an unknown object to a User object.
     * <br><br>
     * This method will call all the other methods in this interface to try and convert the object.
     * <br><br>
     * If it fails to convert, it will throw a {@link ConversionException}.
     *
     * @param object The object to convert.
     * @return The converted User object.
     * @throws ConversionException If the object cannot be converted.
     */
    @Override
    public User convert(Object object) throws ConversionException {
        if(object instanceof IUser) {
            return convert((IUser) object);
        } else if(object instanceof Player) {
            return convert((Player) object);
        } else if(object instanceof String) {
            return convert((String) object);
        } else {
            throw new ConversionException("Cannot convert object of type " + object.getClass().getName() + " to User object.");
        }
    }

    /**
     * Convert from another User object to this sub-plugin's variant of the User object.
     * <br><br>
     * Any sub-plugin that uses this interface should override this method to convert the User object to a local variant.
     * <br><br>
     * The converted object will have all unimplemented variables from the previous object stored as Attributes within the new object.
     * All implemented variables will be copied over to the new object.
     * <br>
     * Attribute-converted variables will be hashed using the specified object's hash method, or a default hash method if the specified object does not have one.
     * <br><br>
     * A {@link ConversionException} will be thrown if the IUser object at any point becomes null, or if the User object cannot be converted.
     *
     * @param user The User object to convert.
     * @return The converted User object.
     * @throws ConversionException If the object cannot be converted.
     */
    @Override
    public User convert(IUser user) throws ConversionException {
        if(user == null) {
            throw new ConversionException("Cannot convert null IUser object to User object.");
        }
        if(user instanceof User) {
            return (User)user;
        }
        user = (IUser)user.pack();
        User newUser = User.fetchEmpty();
        newUser.unpack(user);
        return newUser;
    }

    /**
     * Convert from a Player object to this sub-plugin's variant of the User object.
     * <br><br>
     * Any sub-plugin that uses this interface should override this method to convert the Player object to a local variant.
     * <br>
     * This method is a shortcut for converting a Player object to a User object, then converting the User object to a local variant.
     * <br><br>
     * A {@link ConversionException} will be thrown if the Player object at any point becomes null, or if the User object cannot be converted.
     *
     * @param p The Player object to convert.
     * @return The converted User object.
     * @throws ConversionException If the object cannot be converted.
     */
    @Override
    public User convert(Player p) throws ConversionException {
        if(p == null) {
            throw new ConversionException("Cannot convert null Player object to User object.");
        }
        return User.fetch(p);
    }

    /**
     * Convert from a UUID string / player name to this sub-plugin's variant of the User object.
     * <br><br>
     * Any sub-plugin that uses this interface should override this method to convert the UUID string to a local variant.
     * <br>
     * This method will first try to collect a Player object from the specified String, then call the {@link #convert(Player)} method.
     * <br><br>
     * A {@link ConversionException} will be thrown if the UUID / player name isn't valid / cannot be found, or if the User object cannot be converted.
     *
     * @param name The UUID string / player name to convert.
     * @return The converted User object.
     * @throws ConversionException If the object cannot be converted.
     */
    @Override
    public User convert(String name) throws ConversionException {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.getName().equalsIgnoreCase(name)) {
                return convert(p);
            }
            if(p.getUniqueId().toString().equals(name)) {
                return convert(p);
            }
        }
        throw new ConversionException("Cannot convert " + name + " to User object : No online player matched criteria.");
    }
}
