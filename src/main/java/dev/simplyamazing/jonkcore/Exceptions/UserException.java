package dev.simplyamazing.jonkcore.Exceptions;

import dev.simplyamazing.jonkcore.Objects.Interfaces.IUser;

public class UserException extends Exception {
    private final IUser user;

    public UserException(IUser user, String message) {
        super(message + " {User: " + user + "}");
        this.user = user;
    }

    public IUser getAssociatedUser() {
        return user;
    }
}
