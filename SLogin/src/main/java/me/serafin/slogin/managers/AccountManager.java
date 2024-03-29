package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.database.SQL;
import me.serafin.slogin.database.MySQL;
import me.serafin.slogin.database.SQLite;
import me.serafin.slogin.models.Account;
import me.serafin.slogin.utils.BCrypt;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Logger;

public class AccountManager {

    private final SLogin plugin;
    private final Logger logger;

    private SQL SQL;

    public AccountManager(SLogin plugin) {
        this.plugin = plugin;
        logger = plugin.getLogger();

        this.SQL = setupDatabase();
        if (SQL == null) {
            logger.severe("Error connecting to database! SLogin has been disabled!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    /**
     * Creating database from config information.
     *
     * @return database object
     */
    private SQL setupDatabase() {
        SQL SQL = null;
        try {
            String dataType = plugin.getConfigManager().getConfig().DATATYPE;
            if (dataType.equals("MYSQL")) {
                SQL = new MySQL(plugin.getConfigManager());
            } else {
                SQL = new SQLite(new File(plugin.getDataFolder(), "database.db"));
            }

            SQL.openConnection();
            SQL.update("CREATE TABLE IF NOT EXISTS `slogin_accounts`" +
                    "(`name` VARCHAR(255) NOT NULL PRIMARY KEY, " +
                    "`password` VARCHAR(255) NOT NULL, " +
                    "`email` VARCHAR(255) NULL, " +
                    "`registerIP` TEXT NOT NULL, " +
                    "`registerDate` BIGINT NOT NULL, " +
                    "`lastLoginIP` TEXT NOT NULL, " +
                    "`lastLoginDate` BIGINT NOT NULL)");
            plugin.getLogger().info("Connected to the " + dataType + " database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return SQL;
    }

    /**
     * Closing the database when shutting down the plugin
     */
    public void closeDatabase() {
        if (SQL != null) {
            try {
                SQL.closeConnection();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Reloading database
     */
    public void reloadDatabase() {
        this.SQL = setupDatabase();
    }

    /**
     * Getting player account if exists, otherwise getting empty optional.
     *
     * @param displayName player's name
     * @return optional player's account
     */
    public Optional<Account> getAccount(String displayName) {
        try (ResultSet result = SQL.query("SELECT * FROM `slogin_accounts` WHERE `name` = ?", displayName.toLowerCase())) {
            if (result.next()) {
                return Optional.of(new Account(
                        result.getString("name"),
                        result.getString("password"),
                        result.getString("email"),
                        result.getString("registerIP"),
                        result.getLong("registerDate"),
                        result.getString("lastLoginIP"),
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
     * @param displayName    player's name
     * @param hashedPassword player's hashed password
     * @param IP             player's register IP address
     */
    public void createAccount(String displayName, String hashedPassword, String IP) {
        String command = "INSERT INTO `slogin_accounts` (`name`, `password`, `registerIP`, `registerDate`, `lastLoginIP`, `lastLoginDate`) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            SQL.update(command, displayName.toLowerCase(), hashedPassword, IP, System.currentTimeMillis() + "", IP, System.currentTimeMillis() + "");
        } catch (SQLException exception) {
            exception.printStackTrace();
            logger.severe("Error at command: " + command);
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
        try (ResultSet result = SQL.query("SELECT * FROM `slogin_accounts` WHERE `registerIP` = ?", address)) {
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
     * @param displayName player's name
     * @param dataType    type of data to change
     * @param value       new value
     */
    public void updateAccount(String displayName, DataType dataType, String value) {
        if (dataType == AccountManager.DataType.PASSWORD) {
            value = BCrypt.hashpw(value, BCrypt.gensalt());
        }
        String command = "UPDATE `slogin_accounts` SET `" + dataType.name + "` = ? WHERE `name` = ?";
        try {
            SQL.update(command, value, displayName.toLowerCase());
        } catch (SQLException exception) {
            exception.printStackTrace();
            logger.severe("Error at command: " + command);
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
            SQL.update(command, displayName.toLowerCase());
        } catch (SQLException exception) {
            exception.printStackTrace();
            logger.severe("Error at command: " + command);
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
