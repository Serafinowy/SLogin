package me.serafin.slogin.objects;

import lombok.Data;
import me.serafin.slogin.utils.BCrypt;

@Data
public final class Account {

    private final String displayName, hashedPassword, email, registerIP, lastLoginIP;
    private final long registerDate, lastLoginDate;

    /**
     * Checking if the password is correct
     *
     * @param password player's password
     * @return password is correct
     */
    public boolean comparePassword(String password) {
        return BCrypt.checkpw(password, this.hashedPassword);
    }

}
