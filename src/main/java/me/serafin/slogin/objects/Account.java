package me.serafin.slogin.objects;

import me.serafin.slogin.database.DataBase;
import me.serafin.slogin.utils.BCrypt;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


public class Account {

    private final String displayName, hashedPassword, registerIP, lastLoginIP;
    private final long registerDate, lastLoginDate;

    public Account(String displayName, String hashedPassword, String registerIP, String lastLoginIP, long registerDate, long lastLoginDate) {
        this.displayName = displayName;
        this.hashedPassword = hashedPassword;
        this.registerIP = registerIP;
        this.lastLoginIP = lastLoginIP;
        this.registerDate = registerDate;
        this.lastLoginDate = lastLoginDate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getRegisterIP() {
        return registerIP;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public long getRegisterDate() {
        return registerDate;
    }

    public long getLastLoginDate() {
        return lastLoginDate;
    }

    /**
     * Check if password is correct
     * @param password player's password
     * @return password is correct
     */
    public boolean comparePassword(String password) {
        return BCrypt.checkpw(password, this.hashedPassword);
    }

    ///////////////////////////////////////

    /**
     * Get player account if exists or get empty optional
     * @param name player's name
     * @return optional player's account
     */
    public static Optional<Account> get(DataBase dataBase, String name) {
        try (ResultSet result = dataBase.query("SELECT * FROM `seralogin` WHERE `name` = '" + name.toLowerCase() + "'")) {
            if(result.next()) {
                return Optional.of(new Account(
                        result.getString("name"),
                        result.getString("password"),
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
        String command = "INSERT INTO `seralogin` (`name`, `password`, `registerIP`, `registerDate`, `lastLoginIP`, `lastLoginDate`) VALUES " +
                "('" + name.toLowerCase() + "', '" +
                hashedPassword + "', '" +
                IP + "', '" +
                System.currentTimeMillis() + "', '" +
                IP + "', '" +
                System.currentTimeMillis() + "')";
        try {
            dataBase.update(command);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Bukkit.getLogger().severe("Error at command: " + command);
        }
    }

    /**
     * Updating player's account
     * @param name player's name
     * @param hashedPassword player's hashed password
     * @param lastLoginIP player's last login IP address
     * @param lastLoginDate player's last login date
     */
    public static void update(DataBase dataBase, String name, String hashedPassword, String lastLoginIP, Long lastLoginDate) {
        String command = "UPDATE `seralogin` SET " + (hashedPassword==null ? "" : "`password` = '" + hashedPassword + "', ") +
                (lastLoginIP==null ? "" : "`lastLoginIP` = '" + lastLoginIP + "', ") +
                (lastLoginDate==null ? "" : "`lastLoginDate` = '" + lastLoginDate + "' ") +
                "WHERE `name` = '" + name.toLowerCase() + "'";
        try {
            dataBase.update(command);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Bukkit.getLogger().severe("Error at command: " + command);
        }
    }

    /**
     * Deleting player's account
     * @param name player's name
     */
    public static void delete(DataBase dataBase, String name) {
        String command = "DELETE FROM `seralogin` WHERE `name` = '" + name.toLowerCase() + "'";
        try {
            dataBase.update(command);
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
        try(ResultSet result = dataBase.query("SELECT * FROM `seralogin` WHERE `registerIP` = '" + address + "'")) {
            if(result.next())
                count++;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }
}
