package dev.simplyamazing.jonkcore.Objects;

import java.util.UUID;

public class ID {
    private final String idStr;
    private final UUID idUUID;
    private final long idLong;

    /**
     * Create a new ID object using a string identifier.
     * @param idStr : the string identifier
     */
    public ID(final String idStr) {
        this.idStr = idStr;
        this.idUUID = null;
        this.idLong = 0;
    }

    /**
     * Create a new ID object using a UUID identifier.
     * @param idUUID : the UUID identifier
     */
    public ID(final UUID idUUID) {
        this.idStr = null;
        this.idUUID = idUUID;
        this.idLong = 0;
    }

    /**
     * Create a new ID object using a long identifier.
     * @param idLong : the long identifier
     */
    public ID(final long idLong) {
        this.idStr = null;
        this.idUUID = null;
        this.idLong = idLong;
    }

    /**
     * Check if the ID object is using a string identifier.
     * @return : true if the ID object is using a string identifier
     */
    public boolean isString() {
        return idStr != null;
    }

    /**
     * Check if the ID object is using a UUID identifier.
     * @return : true if the ID object is using a UUID identifier
     */
    public boolean isUUID() {
        return idUUID != null;
    }

    /**
     * Check if the ID object is using a long identifier.
     * @return : true if the ID object is using a long identifier
     */
    public boolean isLong() {
        return idLong != 0;
    }

    /**
     * Get the object that identifies the ID object.
     * @return : the object that identifies the ID object
     */
    public Object getIdentifier() {
        if(isString()) {
            return idStr;
        } else if(isUUID()) {
            return idUUID;
        } else if(isLong()) {
            return idLong;
        }
        return null;
    }

    /**
     * Get the string identifier. [NULLABLE]
     * @return : the string identifier
     */
    public String getString() {
        return idStr;
    }

    /**
     * Get the UUID identifier. [NULLABLE]
     * @return : the UUID identifier
     */
    public UUID getUUID() {
        return idUUID;
    }

    /**
     * Get the long identifier. [NULLABLE]
     * @return : the long identifier
     */
    public long getLong() {
        return idLong;
    }

    /**
     * Convert the ID object to a string format to be used in printing.
     * @return : the string format of the ID object
     */
    public String toString() {
        if(isString()) {
            return idStr;
        } else if(isUUID()) {
            return idUUID.toString();
        } else if(isLong()) {
            return String.valueOf(idLong);
        }
        return null;
    }

    /**
     * Check if the ID object is equal to another object.
     * @param id : the object to compare to
     * @return : true if the ID object is equal to the other object
     */
    public boolean equals(final ID id) {
        if(isString()) {
            return id.isString() && id.getString().equals(idStr);
        } else if(isUUID()) {
            return id.isUUID() && id.getUUID().equals(idUUID);
        } else if(isLong()) {
            return id.isLong() && id.getLong() == idLong;
        }
        return false;
    }

    /**
     * Check if the ID object has the same string identifier.
     * @param id : the string to check
     * @return : true if the ID object has the same string identifier
     */
    public boolean equals(final String id) {
        if(!isString()) return false;
        return idStr.equals(id);
    }

    /**
     * Check if the ID object has the same UUID identifier.
     * @param id : the UUID to check
     * @return : true if the ID object has the same UUID identifier
     */
    public boolean equals(final UUID id) {
        if(!isUUID()) return false;
        return idUUID.equals(id);
    }

    /**
     * Check if the ID object has the same long identifier.
     * @param id : the long to check
     * @return : true if the ID object has the same long identifier
     */
    public boolean equals(final long id) {
        if(!isLong()) return false;
        return idLong == id;
    }
}
