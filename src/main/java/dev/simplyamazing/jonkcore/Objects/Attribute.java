package dev.simplyamazing.jonkcore.Objects;

public class Attribute<T> extends PluginObject {
    private String name;
    private T value;

    /**
     * Initialize a new Attribute object.
     * @param name The name of the attribute.
     * @param value The value of the attribute.
     */
    public Attribute(String name, T value) {
        super(new ID(name + value.toString()));
        this.name = name;
        this.value = value;
    }

    /**
     * Retrieves the name of the attribute.
     * <br><br>
     * This name is used as a pointer to which object variable this Attribute represents.
     * @return The name of the attribute.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the attribute.
     * <br><br>
     * Changing the name of the attribute will change the pointer to which object variable this Attribute represents.
     * <br><br>
     * This method should ideally not be used unless it is affirmed that:
     * <hr>
     * <ol>
     *     <li>The name of the Attribute remains unique within the object it is stored in.</li>
     *     <li>The name of the Attribute matches the name of another variable within the object.</li>
     *     <li>The type of the variable this Attribute now points to is the same as the type of the variable it previously represented.</li>
     * </ol>
     * @param name The new name of the attribute.
     * @return The old name of the attribute.
     */
    @Deprecated
    public String setName(String name) {
        String oldName = this.name;
        this.name = name;
        return oldName;
    }

    /**
     * Retrieves the value of the attribute.
     * <br><br>
     * This value is the same as the value of the object variable this Attribute represents.
     * @return The value of the attribute.
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets the value of the attribute.
     * <br><br>
     * Changing the value of the attribute will change the value of the object variable this Attribute represents.
     * @param value The new value of the attribute.
     * @return The old value of the attribute.
     */
    public T setValue(T value) {
        T oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    /**
     * Retrieve a storable string representation of this Attribute.
     * @return A storable string representation of this Attribute.
     */
    @Override
    public String toString() {
        return name + ":" + value.toString();
    }

    /**
     * Checks if this Attribute is equal to another Attribute.
     * @param obj The Attribute to compare to.
     * @return True if the Attributes are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Attribute<?> attribute) {
            return attribute.name.equals(name) && attribute.value.equals(value);
        }
        return false;
    }

    /**
     * Retrieve an Attribute object from a string representation.
     * <br><br>
     * The string must be in the format of "name:value".
     * @param string The string to parse.
     * @param reference A reference Attribute to use for type checking.
     * @return The Attribute object.
     */
    public static Attribute<?> fromString(String string, Attribute<?> reference) {
        String[] split = string.split(":");
        if(split.length == 2) {
            String name = split[0];
            String value = split[1];
            if(reference.value instanceof Integer) {
                return new Attribute<>(name, Integer.parseInt(value));
            } else if(reference.value instanceof Double) {
                return new Attribute<>(name, Double.parseDouble(value));
            } else if(reference.value instanceof Float) {
                return new Attribute<>(name, Float.parseFloat(value));
            } else if(reference.value instanceof Long) {
                return new Attribute<>(name, Long.parseLong(value));
            } else if(reference.value instanceof Short) {
                return new Attribute<>(name, Short.parseShort(value));
            } else if(reference.value instanceof Byte) {
                return new Attribute<>(name, Byte.parseByte(value));
            } else if(reference.value instanceof Boolean) {
                return new Attribute<>(name, Boolean.parseBoolean(value));
            } else if(reference.value instanceof String) {
                return new Attribute<>(name, value);
            } else if(reference.value instanceof Character) {
                return new Attribute<>(name, value.charAt(0));
            } else if(reference.value instanceof Enum) {
                return new Attribute<>(name, Enum.valueOf((Class<Enum>) reference.value.getClass(), value));
            }
        }
        return new Attribute<>("unknown", "unknown");
    }
}
