package dev.simplyamazing.jonkcore.Objects.Interfaces;

import dev.simplyamazing.jonkcore.Objects.Attribute;
import dev.simplyamazing.jonkcore.Objects.ID;

import java.util.List;

public interface CrossPluginObject {
    /**
     * Get the sub-plugin that this object belongs to.
     * @return The sub-plugin that this object belongs to.
     */
    IJonkPlugin getOriginPlugin();

    /**
     * Get the name of the object that this object is.
     * @return The name of the object.
     */
    String getObjectName();

    /**
     * Pack this object, resulting in a generic object that can be used by other sub-plugins.
     * <br><br>
     * All variables that are not implemented will be stored as Attributes within the object.
     *
     * @return The packed object.
     */
    CrossPluginObject pack();

    /**
     * Unpack this object, resulting in a sub-plugin specific object.
     * <br><br>
     * All variables that are not implemented will be stored as Attributes within the object.
     * Upon unpacking, any Attributes that match a variable within that object will be set to that variable and deleted.
     *
     * @param packed The packed object to unpack.
     */
    void unpack(CrossPluginObject packed);

    /**
     * Retrieve the list of Attributes this CrossPluginObject has.
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
    List<Attribute> getAttributes();

    /**
     * Retrieve an Attribute this CrossPluginObject has based on an ID.
     * <br><br>
     * The ID is used to identify the Attribute, and is normally generated based on the plugin name and variable name separated by a colon.
     * (e.g. <code>myplugin:myvariable</code>)
     *
     * @param id The ID of the Attribute to retrieve.
     * @return The Attribute this CrossPluginObject has based on an ID.
     */
    Attribute getAttribute(ID id);

    /**
     * Add an Attribute to this CrossPluginObject.
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
     * @param attribute The Attribute to add to this CrossPluginObject.
     */
    void addAttribute(Attribute attribute);

    /**
     * Remove an Attribute from this CrossPluginObject using its ID.
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
    void removeAttribute(ID id);

    /**
     * Remove an Attribute from this CrossPluginObject.
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
    void removeAttribute(Attribute attribute);

    /**
     * Check if this CrossPluginObject has a particular Attribute using its ID.
     *
     * @param id The ID of the Attribute to check for.
     * @return True if this CrossPluginObject has the Attribute, false otherwise.
     */
    boolean hasAttribute(ID id);

    /**
     * Check if this CrossPluginObject has a particular Attribute.
     *
     * @param attribute The Attribute to check for.
     * @return True if this CrossPluginObject has the Attribute, false otherwise.
     */
    boolean hasAttribute(Attribute attribute);
}
