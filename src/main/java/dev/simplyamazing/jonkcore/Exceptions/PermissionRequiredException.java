package dev.simplyamazing.jonkcore.Exceptions;

import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;

public class PermissionRequiredException extends UserException {
    private final String permission;

    public PermissionRequiredException(IUser user, String permission, String message) {
        super(user, message + " {Permission: " + permission + "}");
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
