package dev.simplyamazing.jonkcore.Objects.Interfaces;

import java.net.URL;

public interface IJonkPlugin {

    /**
     * Retrieve the UserConverter object of the sub-plugin.
     * <br><br>
     * The UserConverter object is used to convert between Bukkit's Player objects, other generic JonkCORE User objects and hashcodes to the sub-plugin's
     * variant of the User object. It is particularly useful in cross-plugin communication, as it enables different sub-plugins to convert generic User objects back
     * and forth.
     *
     * @return UserConverter object of the sub-plugin
     */
    IUserConverter getUserConverter();

    /**
     * Retrieve the Storage object of the sub-plugin.
     * <br><br>
     * This object acts as the soft-storage system within the sub-plugin, holding information about the sub-plugin's users and other data.
     *
     * @return Storage object of the sub-plugin
     */
    IStorage getStorage();

    /**
     * Retrieve the prefix all messages sent by the sub-plugin will have.
     * <br><br>
     * This is useful for differentiating chat messages coming from different plugins, and it's position as the first line within a message is absolute.
     *
     * @return prefix of the sub-plugin
     */
    String getPrefix();

    /**
     * Retrieve the version number of the sub-plugin, normally in the format of (major).(minor).(patch).
     * <br><br>
     * The version number is usually used as both a reference for the UpdateChecker to check for updates, and to identify the plugin in the console.
     * In most situations, this should link to the version number in the plugin.yml file.
     *
     * @return version number of the sub-plugin
     */
    String getVersion();

    /**
     * Retrieve the author of the sub-plugin.
     * <br><br>
     * In most situations, this should link to the author in the plugin.yml file.
     * In the case of multiple authors, this should return authors separated by a comma.
     *
     * @return author of the sub-plugin
     */
    String getAuthor();

    /**
     * Retrieve the GitHub page of the sub-plugin.
     * <br><br>
     * This is most commonly used as a reference for the UpdateChecker to locate the latest release version.
     *
     * @return GitHub page of the sub-plugin
     */
    URL getRepository();
}
