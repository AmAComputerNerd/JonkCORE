package dev.simplyamazing.jonkcore.Objects;

import dev.simplyamazing.jonkcore.Objects.Interfaces.IChatRoom;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IChatStyling;
import dev.simplyamazing.jonkcore.Utilities.ChatUtilities;
import org.bukkit.ChatColor;

import java.util.List;

public class ChatStyle implements IChatStyling {
    private String prefix;
    private String suffix;
    private ChatColor colour;
    private boolean canOverrideColour;

    /**
     * Instantiate a new DEFAULT ChatStyle.
     */
    public ChatStyle() {
        this("", "", ChatColor.WHITE, false);
    }

    /**
     * Instantiate a new ChatStyle with custom styling and default colour.
     * @param prefix The prefix to use.
     * @param suffix The suffix to use.
     * @param canOverrideColour Whether or not the user can override the colour.
     */
    public ChatStyle(String prefix, String suffix, boolean canOverrideColour) {
        this(prefix, suffix, ChatColor.WHITE, canOverrideColour);
    }

    /**
     * Instantiate a new ChatStyle with custom styling and colour.
     * @param prefix The prefix to use.
     * @param suffix The suffix to use.
     * @param colour The colour to use.
     * @param canOverrideColour Whether or not the user can override the colour.
     */
    public ChatStyle(String prefix, String suffix, ChatColor colour, boolean canOverrideColour) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.colour = colour;
        this.canOverrideColour = canOverrideColour;
    }

    /**
     * Retrieves the prefix of the ChatStyling object.
     * <br><br>
     * The prefix is automatically coloured and formatted using the default colour chat '&'.
     *
     * @return The prefix of the ChatStyling object.
     */
    @Override
    public String getPrefix() {
        return prefix;
    }

    /**
     * Set a new prefix for the ChatStyling object.
     * <br><br>
     * This prefix is automatically coloured and formatted using the default colour chat '&'.
     *
     * @param prefix The new prefix for the ChatStyling object.
     */
    @Override
    public void setPrefix(String prefix) {
        this.prefix = ChatUtilities.autoColour(prefix);
    }

    /**
     * Checks if the ChatStyling object has a prefix.
     *
     * @return True if the ChatStyling object has a prefix, false otherwise.
     */
    @Override
    public boolean hasPrefix() {
        return prefix != null;
    }

    /**
     * Retrieves the suffix of the ChatStyling object.
     * <br><br>
     * The suffix is automatically coloured and formatted using the default colour chat '&'.
     *
     * @return The suffix of the ChatStyling object.
     */
    @Override
    public String getSuffix() {
        return suffix;
    }

    /**
     * Set a new suffix for the ChatStyling object.
     * <br><br>
     * This suffix is automatically coloured and formatted using the default colour chat '&'.
     *
     * @param suffix The new suffix for the ChatStyling object.
     */
    @Override
    public void setSuffix(String suffix) {
        this.suffix = ChatUtilities.autoColour(suffix);
    }

    /**
     * Checks if the ChatStyling object has a suffix.
     *
     * @return True if the ChatStyling object has a suffix, false otherwise.
     */
    @Override
    public boolean hasSuffix() {
        return suffix != null;
    }

    /**
     * Retrieves the default colour of the ChatStyling object.
     * <br><br>
     * This colour is automatically applied to any message using the {@link #formatMessage(String)} and {@link #formatMessageAnonymously(String)} methods.
     * <br>
     * If colour overriding is enabled, this colour can be overridden using message formatting codes
     *
     * @return The default colour of the ChatStyling object.
     */
    @Override
    public ChatColor getDefaultColour() {
        return colour;
    }

    /**
     * Set a new default colour for the ChatStyling object.
     * <br><br>
     * This colour is automatically applied to any message using the {@link #formatMessage(String)} and {@link #formatMessageAnonymously(String)} methods.
     * <br>
     * If colour overriding is enabled, this colour can be overridden using message formatting codes
     *
     * @param colour The new default colour for the ChatStyling object.
     */
    @Override
    public void setDefaultColour(ChatColor colour) {
        this.colour = colour;
    }

    /**
     * Set a new default colour for the ChatStyling object.
     * <br><br>
     * This colour is automatically applied to any message using the {@link #formatMessage(String)} and {@link #formatMessageAnonymously(String)} methods.
     * <br>
     * If colour overriding is enabled, this colour can be overridden using message formatting codes
     *
     * @param colour The char representing the formatting code of the new default colour for the ChatStyling object.
     */
    @Override
    public void setDefaultColour(char colour) {
        this.colour = ChatColor.getByChar(colour);
    }

    /**
     * Checks if the ChatStyling object has a default colour.
     *
     * @return True if the ChatStyling object has a default colour, false otherwise.
     */
    @Override
    public boolean hasDefaultColour() {
        return colour != null;
    }

    /**
     * Checks if the ChatStyling object has colour overriding enabled.
     * <br><br>
     * If colour overriding is enabled, the default colour can be overridden using message formatting codes.
     *
     * @return True if the ChatStyling object has colour overriding enabled, false otherwise.
     */
    @Override
    public boolean canDoColourOverride() {
        return canOverrideColour;
    }

    /**
     * Change whether the ChatStyling object allows colour overriding.
     * <br><br>
     * If colour overriding is enabled, the default colour can be overridden using message formatting codes.
     *
     * @param allow True to allow colour overriding, false otherwise.
     */
    @Override
    public void setAllowColourOverride(boolean allow) {
        this.canOverrideColour = allow;
    }

    /**
     * Formats a message using the ChatStyling object.
     * <br><br>
     * This method will automatically apply the prefix, suffix and default colour to the message.
     *
     * @param message The message to format.
     * @return The formatted message.
     */
    @Override
    public String formatMessage(String message) {
        if(message == null) return "";
        if(colour != null) message = colour + message;
        if(prefix != null) message = prefix + message;
        if(suffix != null) message = message + suffix;
        if(canOverrideColour) message = ChatUtilities.autoColour(message);
        return message;
    }

    /**
     * Formats a message using the ChatStyling object.
     * <br><br>
     * This method will automatically apply the default colour to the message without the prefix or suffix. This is best used in
     * styling a message that already has a prefix and suffix applied.
     *
     * @param message The message to format.
     * @return The formatted message.
     */
    @Override
    public String formatMessageAnonymously(String message) {
        if(message == null) return "";
        if(colour != null) message = colour + message;
        if(canOverrideColour) message = ChatUtilities.autoColour(message);
        return message;
    }
}
