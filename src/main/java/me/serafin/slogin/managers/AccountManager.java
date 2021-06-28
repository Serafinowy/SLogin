package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.database.DataBase;
import me.serafin.slogin.database.MySQL;
import me.serafin.slogin.database.SQLite;
import me.serafin.slogin.objects.Account;
import me.serafin.slogin.objects.Lang;
import me.serafin.slogin.utils.BCrypt;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class AccountManager {

    private final SLogin plugin;
    private DataBase dataBase;

    public AccountManager() {
        this.plugin = SLogin.getInstance();
        this.dataBase = setupDatabase();
        if (dataBase == null) {
            plugin.getLogger().severe("Error connecting to database! SLogin has been disabled!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

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

    /**
     * Creating database from config information.
     * @return database object
     */
    private DataBase setupDatabase() {
        DataBase dataBase = null;
        try {
            assert plugin.getConfigManager().getDATATYPE() != null;
            if (plugin.getConfigManager().getDATATYPE().equals("MYSQL")) {
                dataBase = new MySQL(plugin.getConfigManager());
            } else {
                dataBase = new SQLite(new File(plugin.getDataFolder(), "database.db"));
            }

            dataBase.openConnection();
            dataBase.update("CREATE TABLE IF NOT EXISTS `slogin_accounts`" +
                    "(`name` VARCHAR(255) NOT NULL PRIMARY KEY, " +
                    "`password` VARCHAR(255) NOT NULL, " +
                    "`email` VARCHAR(255) NULL, " +
                    "`registerIP` TEXT NOT NULL, " +
                    "`registerDate` BIGINT NOT NULL, " +
                    "`lastLoginIP` TEXT NOT NULL, " +
                    "`lastLoginDate` BIGINT NOT NULL)");
            plugin.getLogger().info("Connected to the " + plugin.getConfigManager().getDATATYPE() + " database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataBase;
    }

    /**
     * Closing the database when shutting down the plugin
     */
    public void closeDatabase() {
        if (dataBase != null) {
            try {
                dataBase.closeConnection();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Reloading database
     */
    public void reloadDatabase() {
        this.dataBase = setupDatabase();
    }

    /**
     * Getting player account if exists, otherwise getting empty optional.
     *
     * @param name player's name
     * @return optional player's account
     */
    public Optional<Account> getAccount(String name) {
        try (ResultSet result = dataBase.query("SELECT * FROM `slogin_accounts` WHERE `name` = ?", name.toLowerCase())) {
            if (result.next()) {
                return Optional.of(new Account(
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
    public void createAccount(String name, String hashedPassword, String IP) {
        String command = "INSERT INTO `slogin_accounts` (`name`, `password`, `registerIP`, `registerDate`, `lastLoginIP`, `lastLoginDate`) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            dataBase.update(command, name.toLowerCase(), hashedPassword, IP, System.currentTimeMillis() + "", IP, System.currentTimeMillis() + "");
        } catch (SQLException exception) {
            exception.printStackTrace();
            Bukkit.getLogger().severe("Error at command: " + command);
        }
    }

    /**
     * Getting accounts registered from the same IP address
     *
     * @param address account's IP address
     * @return number of accounts
     */
    public int accountIPCount(String address) {
        int count = 0;
        try (ResultSet result = dataBase.query("SELECT * FROM `slogin_accounts` WHERE `registerIP` = ?", address)) {
            while (result.next()) count++;
            return count;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return count;
    }

    /**
     * Updating player's account
     *
     * @param dataType type of data to change
     * @param value    new value
     */
    public void updateAccount(String displayName, DataType dataType, String value) {
        if (dataType == AccountManager.DataType.PASSWORD) {
            value = BCrypt.hashpw(value, BCrypt.gensalt());
        }
        String command = "UPDATE `slogin_accounts` SET `" + dataType.name + "` = ? WHERE `name` = ?";
        try {
            dataBase.update(command, value, displayName.toLowerCase());
        } catch (SQLException exception) {
            exception.printStackTrace();
            Bukkit.getLogger().severe("Error at command: " + command);
        }
    }

    public void updateAccount(Account account, DataType dataType, String value) {
        updateAccount(account.getDisplayName(), dataType, value);
    }

    /**
     * Deleting player's account from database
     *
     * @param displayName player's name
     */
    public void deleteAccount(String displayName) {
        String command = "DELETE FROM `slogin_accounts` WHERE `name` = ?";
        try {
            dataBase.update(command, displayName.toLowerCase());
        } catch (SQLException exception) {
            exception.printStackTrace();
            Bukkit.getLogger().severe("Error at command: " + command);
        }
    }

    public void deleteAccount(Account account) {
        deleteAccount(account.getDisplayName());
    }

    ///////////////////////////////////////

    public enum DataType {
        PASSWORD("password"),
        EMAIL("email"),
        LAST_LOGIN_IP("lastLoginIP"),
        LAST_LOGIN_DATE("lastLoginDate");

        private final String name;

        DataType(String name) {
            this.name = name;
        }
    }

}
