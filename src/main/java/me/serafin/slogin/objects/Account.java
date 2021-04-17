package me.serafin.slogin.objects;

import lombok.Data;
import me.serafin.slogin.database.DataBase;
import me.serafin.slogin.utils.BCrypt;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Data
public final class Account {

    private final DataBase dataBase;

    private final String displayName, hashedPassword, email, registerIP, lastLoginIP;
    private final long registerDate, lastLoginDate;

    /**
     * Check if password is correct
     *
     * @param password player's password
     * @return password is correct
     */
    public boolean comparePassword(String password) {
        return BCrypt.checkpw(password, this.hashedPassword);
    }

    /**
     * Update player's account
     *
     * @param dataType type of data to change
     * @param value    new value
     */
    public void update(DataType dataType, String value) {
        String set = "";
        switch (dataType) {
            case PASSWORD:
                set = "`password` = ?";
                String salt = BCrypt.gensalt();
                value = BCrypt.hashpw(value, salt);
                break;
            case EMAIL:
                set = "`email` = ?";
                break;
            case LAST_LOGIN_IP:
                set = "`lastLoginIP` = ?";
                break;
            case LAST_LOGIN_DATE:
                set = "`lastLoginDate` = ?";
                break;
        }
        String command = "UPDATE `slogin_accounts` SET " + set + " WHERE `name` = ?";
        try {
            dataBase.update(command, value, this.displayName.toLowerCase());
        } catch (SQLException exception) {
            exception.printStackTrace();
            Bukkit.getLogger().severe("Error at command: " + command);
        }
    }

    /**
     * Delete player's account from database
     */
    public void delete() {
        String command = "DELETE FROM `slogin_accounts` WHERE `name` = ?";
        try {
            dataBase.update(command, this.displayName.toLowerCase());
        } catch (SQLException exception) {
            exception.printStackTrace();
            Bukkit.getLogger().severe("Error at command: " + command);
        }
    }

    ///////////////////////////////////////

    /**
     * Get player account if exists or get empty optional
     *
     * @param name player's name
     * @return optional player's account
     */
    public static Optional<Account> get(DataBase dataBase, String name) {
        try (ResultSet result = dataBase.query("SELECT * FROM `slogin_accounts` WHERE `name` = ?", name.toLowerCase())) {
            if (result.next()) {
                return Optional.of(new Account(
                        dataBase,
                        result.getString("name"),
                        result.getString("password"),
                        result.getString("email"),
                        result.getString("registerIP"),
                        result.getString("lastLoginIP"),
                        result.getLong("registerDate"),
                        result.getLong("lastLoginDate")));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Creating new account
     *
     * @param name           player's name
     * @param hashedPassword player's hashed password
     * @param IP             player's register IP address
     */
    public static void create(DataBase dataBase, String name, String hashedPassword, String IP) {
        String command = "INSERT INTO `slogin_accounts` (`name`, `password`, `registerIP`, `registerDate`, `lastLoginIP`, `lastLoginDate`) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            dataBase.update(command, name.toLowerCase(), hashedPassword, IP, System.currentTimeMillis() + "", IP, System.currentTimeMillis() + "");
        } catch (SQLException exception) {
            exception.printStackTrace();
            Bukkit.getLogger().severe("Error at command: " + command);
        }
    }

    /**
     * Get account registered from the same IP address
     *
     * @param address account's IP address
     * @return number of accounts
     */
    public static int accountIPCount(DataBase dataBase, String address) {
        int count = 0;
        try (ResultSet result = dataBase.query("SELECT * FROM `slogin_accounts` WHERE `registerIP` = ?", address)) {
            while (result.next()) count++;
            return count;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return count;
    }

    ///////////////////////////////////////

    /**
     * Format placeholders in string.
     *
     * @param account player's account
     * @param pattern string to format
     * @return formatted string
     */
    public static String formatData(Account account, String pattern, Lang lang) {

        String datePattern = "dd.MM.yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        Date registerDate = new Date(account.getRegisterDate());
        Date lastLoginDate = new Date(account.getLastLoginDate());

        return pattern.replace("{PLAYER}", account.getDisplayName().toUpperCase())
                .replace("{EMAIL}", account.getEmail() == null ? lang.misc_nullValue : account.getEmail())
                .replace("{REGISTER_IP}", account.getRegisterIP())
                .replace("{REGISTER_DATE}", simpleDateFormat.format(registerDate))
                .replace("{LASTLOGIN_IP}", account.getLastLoginIP())
                .replace("{LASTLOGIN_DATE}", simpleDateFormat.format(lastLoginDate));
    }

    public enum DataType {
        PASSWORD, EMAIL, LAST_LOGIN_IP, LAST_LOGIN_DATE
    }
}
