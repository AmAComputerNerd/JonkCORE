package dev.simplyamazing.jonkcore.Objects.Interfaces;

import org.bukkit.ChatColor;

public interface IChatStyling {
    /**
     * Retrieves the prefix of the ChatStyling object.
     * <br><br>
     * The prefix is automatically coloured and formatted using the default colour chat '&'.
     *
     * @return The prefix of the ChatStyling object.
     */
    String getPrefix();

    /**
     * Set a new prefix for the ChatStyling object.
     * <br><br>
     * This prefix is automatically coloured and formatted using the default colour chat '&'.
     *
     * @param prefix The new prefix for the ChatStyling object.
     */
    void setPrefix(String prefix);

    /**
     * Checks if the ChatStyling object has a prefix.
     * @return True if the ChatStyling object has a prefix, false otherwise.
     */
    boolean hasPrefix();

    /**
     * Retrieves the suffix of the ChatStyling object.
     * <br><br>
     * The suffix is automatically coloured and formatted using the default colour chat '&'.
     *
     * @return The suffix of the ChatStyling object.
     */
    String getSuffix();

    /**
     * Set a new suffix for the ChatStyling object.
     * <br><br>
     * This suffix is automatically coloured and formatted using the default colour chat '&'.
     *
     * @param suffix The new suffix for the ChatStyling object.
     */
    void setSuffix(String suffix);

    /**
     * Checks if the ChatStyling object has a suffix.
     * @return True if the ChatStyling object has a suffix, false otherwise.
     */
    boolean hasSuffix();

    /**
     * Retrieves the default colour of the ChatStyling object.
     * <br><br>
     * This colour is automatically applied to any message using the {@link #formatMessage(String)} and {@link #formatMessageAnonymously(String)} methods.
     * <br>
     * If colour overriding is enabled, this colour can be overridden using message formatting codes
     *
     * @return The default colour of the ChatStyling object.
     */
    ChatColor getDefaultColour();

    /**
     * Set a new default colour for the ChatStyling object.
     * <br><br>
     * This colour is automatically applied to any message using the {@link #formatMessage(String)} and {@link #formatMessageAnonymously(String)} methods.
     * <br>
     * If colour overriding is enabled, this colour can be overridden using message formatting codes
     *
     * @param colour The new default colour for the ChatStyling object.
     */
    void setDefaultColour(ChatColor colour);

    /**
     * Set a new default colour for the ChatStyling object.
     * <br><br>
     * This colour is automatically applied to any message using the {@link #formatMessage(String)} and {@link #formatMessageAnonymously(String)} methods.
     * <br>
     * If colour overriding is enabled, this colour can be overridden using message formatting codes
     *
     * @param colour The char representing the formatting code of the new default colour for the ChatStyling object.
     */
    void setDefaultColour(char colour);

    /**
     * Checks if the ChatStyling object has a default colour.
     * @return True if the ChatStyling object has a default colour, false otherwise.
     */
    boolean hasDefaultColour();

    /**
     * Checks if the ChatStyling object has colour overriding enabled.
     * <br><br>
     * If colour overriding is enabled, the default colour can be overridden using message formatting codes.
     *
     * @return True if the ChatStyling object has colour overriding enabled, false otherwise.
     */
    boolean canDoColourOverride();
    /**
     * Change whether the ChatStyling object allows colour overriding.
     * <br><br>
     * If colour overriding is enabled, the default colour can be overridden using message formatting codes.
     *
     * @param allow True to allow colour overriding, false otherwise.
     */
    void setAllowColourOverride(boolean allow);

    /**
     * Formats a message using the ChatStyling object.
     * <br><br>
     * This method will automatically apply the prefix, suffix and default colour to the message.
     *
     * @param message The message to format.
     * @return The formatted message.
     */
    String formatMessage(String message);

    /**
     * Formats a message using the ChatStyling object.
     * <br><br>
     * This method will automatically apply the default colour to the message without the prefix or suffix. This is best used in
     * styling a message that already has a prefix and suffix applied.
     *
     * @param message The message to format.
     * @return The formatted message.
     */
    String formatMessageAnonymously(String message);
}
