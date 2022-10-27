package dev.simplyamazing.jonkcore.Objects;

public abstract class PluginObject {
    private final ID identifier;
    private boolean locked;
    private boolean parentLock;
    private boolean equivalentToNull;

    /**
     * Instantiate default values.
     *
     * @param id the UUID of the object
     */
    protected PluginObject(ID id) {
        this.identifier = id;
        this.locked = false;
        this.parentLock = false;
        this.equivalentToNull = false;
    }

    /**
     * Instantiate default values, and dictate whether an object is locked.
     *
     * @param id the UUID of the object
     * @param locked whether it should be locked
     */
    protected PluginObject(ID id, boolean locked) {
        this.identifier = id;
        this.locked = locked;
        this.parentLock = false;
        this.equivalentToNull = false;
    }

    /**
     * Instantiate and dictate all custom variables.
     *
     * @param id the UUID of the object
     * @param locked whether it should be locked
     * @param parentLock whether the lock status can be changed
     * @param equivalentToNull whether the object is equivalent to null
     */
    protected PluginObject(ID id, boolean locked, boolean parentLock, boolean equivalentToNull) {
        this.identifier = id;
        this.locked = locked;
        this.parentLock = parentLock;
        this.equivalentToNull = equivalentToNull;
    }

    /**
     * Get the ID of the object.
     *
     * @return the ID of the object
     */
    public ID getIdentifier() {
        return identifier;
    }

    /**
     * Check whether this object is locked.
     *
     * @return true if the object is locked
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Set the lock status of the object.
     *
     * @param val the new value
     * @throws NoSuchMethodException when the object value `parentLock` is false
     */
    public void setLocked(boolean val) throws NoSuchMethodException {
        if(parentLock) throw new NoSuchMethodException("Object lock status cannot be changed.");
        this.locked = val;
    }

    /**
     * Override the value `parentLock` and change it to `true`. Cannot be changed back afterward.
     */
    public void enableParentLock() {
        this.parentLock = true;
    }

    /**
     * Check whether the object has been perma-locked with `enableParentLock()`.
     *
     * @return true if the object is perma-locked
     */
    public boolean isPermaLocked() {
        return parentLock;
    }

    /**
     * Check whether this object is equivalent to null.
     *
     * @return true if the object is equivalent to null
     */
    public boolean isEquivalentToNull() {
        return equivalentToNull;
    }

    /**
     * Set whether this object is equivalent to null.
     *
     * @param val the new value
     * @throws NoSuchMethodException when the object value `parentLock` is false
     */
    public void setEquivalentToNull(boolean val) throws NoSuchMethodException {
        if(parentLock) throw new NoSuchMethodException("Object null equivalence status cannot be changed.");
        this.equivalentToNull = val;
    }
}
