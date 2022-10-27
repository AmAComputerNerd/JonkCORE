package dev.simplyamazing.jonkcore.Utilities;

import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;
import org.bukkit.entity.Player;

public class PermissionUtils {
    /**
     * Check if a User has a permission. This only returns true if the User has the permission, regardless of parent permissions or wildcards.
     * @param u : User to check
     * @param permission : permission to check
     * @return : true if the User has the permission, false otherwise
     */
    public static boolean checkOnly(IUser u, String permission) {
        if(permission == null || permission.equals("")) return true;
        return u.safeGetLegacy().hasPermission(permission);
    }

    /**
     * Legacy method for checking if a Player has a permission. This only returns true if the Player has the permission, regardless of parent permissions or wildcards.
     * <br><br>
     * This method is only used for backwards compatibility, the preferred method is {@link #checkOnly(IUser, String)}.
     * @param p : Player to check
     * @param permission : permission to check
     * @return : true if the Player has the permission, false otherwise
     */
    @Deprecated
    public static boolean legacyCheckOnly(Player p, String permission) {
        if(permission == null || permission.equals("")) return true;
        return p.hasPermission(permission);
    }

    /**
     * Check if a User DOES NOT have a permission. This only returns true if the User does not have the permission, regardless of parent permissions or wildcards.
     * @param u : User to check
     * @param permission : permission to check
     * @return : true if the User does not have the permission, false otherwise
     */
    public static boolean checkNotOnly(IUser u, String permission) {
        if(permission == null || permission.equals("")) return false;
        return !u.safeGetLegacy().hasPermission(permission);
    }

    /**
     * Check if a User has a permission OR is op. This only returns true if the User has the permission or is op, regardless of parent permissions or wildcards.
     * @param u : User to check
     * @param permission : permission to check
     * @return : true if the User has the permission or is op, false otherwise
     */
    public static boolean checkOp(IUser u, String permission) {
        if(checkOnly(u, permission)) return true;
        return u.safeGetLegacy().isOp();
    }

    /**
     * Legacy method for checking if a Player has a permission OR is op. This only returns true if the Player has the permission or is op, regardless of parent permissions or wildcards.
     * <br><br>
     * This method is only used for backwards compatibility, the preferred method is {@link #checkOp(IUser, String)}.
     * @param u : Player to check
     * @param permission : permission to check
     * @return : true if the Player has the permission or is op, false otherwise
     */
    public static boolean legacyCheckOp(Player u, String permission) {
        if(legacyCheckOnly(u, permission)) return true;
        return u.isOp();
    }

    /**
     * Check if a User DOES NOT have a permission OR is NOT op. This only returns true if the User does not have the permission or is op, regardless of parent permissions or wildcards.
     * @param u : User to check
     * @param permission : permission to check
     * @return : true if the User does not have the permission or is op, false otherwise
     */
    public static boolean checkNotOp(IUser u, String permission) {
        if(checkNotOnly(u, permission)) return true;
        return !u.safeGetLegacy().isOp();
    }

    /**
     * Check if a User has a permission, parent permission, or is op.
     * @param u : User to check
     * @param permission : permission to check
     * @return : true if the User has the permission, parent permission, or is op, false otherwise
     */
    public static boolean checkParent(IUser u, String permission) {
        if(checkOp(u, permission)) return true;
        return checkOnly(u, splitBackwards(permission, 1));
    }

    /**
     * Legacy method for checking if a Player has a permission, parent permission, or is op.
     * <br><br>
     * This method is only used for backwards compatibility, the preferred method is {@link #checkParent(IUser, String)}.
     * @param p : Player to check
     * @param permission : permission to check
     * @return : true if the Player has the permission, parent permission, or is op, false otherwise
     */
    @Deprecated
    public static boolean legacyCheckParent(Player p, String permission) {
        if(legacyCheckOnly(p, permission) || p.isOp()) return true;
        return legacyCheckOnly(p, splitBackwards(permission, 1));
    }

    /**
     * Check if a User DOES NOT have a permission, parent permission, or is NOT op.
     * @param u : User to check
     * @param permission : permission to check
     * @return : true if the User does not have the permission, parent permission, or is op, false otherwise
     */
    public static boolean checkNotParent(IUser u, String permission) {
        if(checkNotOp(u, permission)) return true;
        return checkNotOnly(u, splitBackwards(permission, 1));
    }

    /**
     * Check if a User has a permission in any way, whether it be the permission itself, a parent permission, operator, or a wildcard.
     * @param u : User to check
     * @param permission : permission to check
     * @return : true if the User has the permission, parent permission, or is op, false otherwise
     */
    public static boolean checkAny(IUser u, String permission) {
        if(checkOnly(u, "*")) return true;
        else if(checkParent(u, permission)) return true;
        return checkOnly(u, split(permission, 1));
    }

    /**
     * Legacy method for checking if a Player has a permission in any way, whether it be the permission itself, a parent permission, operator or a wildcard.
     * <br><br>
     * This method is only used for backwards compatibility, the preferred method is {@link #checkAny(IUser, String)}.
     * @param p : Player to check
     * @param permission : permission to check
     * @return : true if the Player has the permission, parent permission, or is op, false otherwise
     */
    @Deprecated
    public static boolean legacyCheckAny(Player p, String permission) {
        if(legacyCheckOnly(p, "*")) return true;
        else if(legacyCheckParent(p, permission)) return true;
        return legacyCheckOnly(p, split(permission, 1));
    }

    /**
     * Check if a User DOES NOT have a permission in any way, whether it be the permission itself, a parent permission, operator, or a wildcard.
     * @param u : User to check
     * @param permission : permission to check
     * @return : true if the User does not have the permission, parent permission, or is op, false otherwise
     */
    public static boolean checkNotAny(IUser u, String permission) {
        if (checkNotOnly(u, "*")) return true;
        else if (checkNotParent(u, permission)) return true;
        return checkNotOnly(u, split(permission, 1));
    }

    /**
     * Split a permission down to a certain depth, from front to back (i.e. when permission = "jonkcore.chats.general.mute" & dots = 1, result is "jonkcore.").
     * @param permission : permission to split
     * @param dots : depth to split to
     * @return : the split permission
     */
    private static String split(String permission, int dots) {
        String[] split = permission.split("\\.");
        if(split.length < (dots-1) || dots <= 0) return permission;
        StringBuilder constructed = new StringBuilder();
        for(int i = 0; i < dots; i++) {
            constructed.append(split[i]).append(".");
        }
        return constructed.toString();
    }

    /**
     * Split a permission down to a certain depth, from back to front (i.e. when permission = "jonkcore.chats.general.mute" & dots = 1, result is "jonkcore.chats.general.").
     * @param permission : permission to split
     * @param dots : depth to split to
     * @return : the split permission
     */
    private static String splitBackwards(String permission, int dots) {
        String[] split = permission.split("\\.");
        if(split.length < (dots-1) || dots <= 0) return permission;
        StringBuilder constructed = new StringBuilder();
        for(int i = 0; i < split.length-dots; i++) {
            constructed.append(split[i]).append(".");
        }
        return constructed.toString();
    }
}
