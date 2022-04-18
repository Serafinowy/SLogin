package me.serafin.slogin.models;

import lombok.Data;
import me.serafin.slogin.utils.BCrypt;

@Data
public final class Account {

    private final String displayName;
    private final String hashedPassword;
    private final String email;

    private final String registerIP;
    private final long registerDate;

    private final String lastLoginIP;
    private final long lastLoginDate;

    /**
     * Checking if the password is correct.
     *
     * @param password player's password
     * @return password is correct
     */
    public boolean comparePassword(String password) {
        return BCrypt.checkpw(password, this.hashedPassword);
    }

}
