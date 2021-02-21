package me.serafin.slogin.objects;

import lombok.Data;
import me.serafin.slogin.database.DataBase;
import me.serafin.slogin.utils.BCrypt;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Data
public final class Account {

    private final String displayName, hashedPassword, email, registerIP, lastLoginIP;
    private final long registerDate, lastLoginDate;

    /**
     * Check if password is correct
     * @param password player's password
     * @return password is correct
     */
    public boolean comparePassword(String password) {
        return BCrypt.checkpw(password, this.hashedPassword);
    }

    /**
     * Updating player's account
     * @param hashedPassword player's hashed password
     * @param lastLoginIP player's last login IP address
     * @param lastLoginDate player's last login date
     */
    public void update(DataBase dataBase, String hashedPassword, String email, String lastLoginIP, Long lastLoginDate) {
        String command = "UPDATE `slogin_accounts` SET `password` = ?, `email` = ?, `lastLoginIP` = ?, `lastLoginDate` = ? WHERE `name` = ?";
        try {
            dataBase.update(command, hashedPassword, email, lastLoginIP, lastLoginDate + "", this.displayName.toLowerCase());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Bukkit.getLogger().severe("Error at command: " + command);
        }
    }

    /**
     * Deleting player's account
     */
    public void delete(DataBase dataBase) {
        String command = "DELETE FROM `slogin_accounts` WHERE `name` = ?";
        try {
            dataBase.update(command, this.displayName.toLowerCase());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Bukkit.getLogger().severe("Error at command: " + command);
        }
    }

    ///////////////////////////////////////

    /**
     * Get player account if exists or get empty optional
     * @param name player's name
     * @return optional player's account
     */
    public static Optional<Account> get(DataBase dataBase, String name) {
        try (ResultSet result = dataBase.query("SELECT * FROM `slogin_accounts` WHERE `name` = ?", name.toLowerCase())) {
            if(result.next()) {
                return Optional.of(new Account(
                        result.getString("name"),
                        result.getString("password"),
                        result.getString("email"),
                        result.getString("registerIP"),
                        result.getString("lastLoginIP"),
                        result.getLong("registerDate"),
                        result.getLong("lastLoginDate")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Creating new account
     * @param name player's name
     * @param hashedPassword player's hashed password
     * @param IP player's register IP address
     */
    public static void create(DataBase dataBase, String name, String hashedPassword, String IP) {
        String command = "INSERT INTO `slogin_accounts` (`name`, `password`, `registerIP`, `registerDate`, `lastLoginIP`, `lastLoginDate`) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            dataBase.update(command, name.toLowerCase(), hashedPassword, IP, System.currentTimeMillis()+"", IP, System.currentTimeMillis()+"");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Bukkit.getLogger().severe("Error at command: " + command);
        }
    }

    /**
     * Get account registered from the same IP address
     * @param address account's IP address
     * @return number of accounts
     */
    public static int accountIPCount(DataBase dataBase, String address){
        int count = 0;
        try(ResultSet result = dataBase.query("SELECT * FROM `slogin_accounts` WHERE `registerIP` = ?", address)) {
            while (result.next()) count++;
            return count;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }
}
