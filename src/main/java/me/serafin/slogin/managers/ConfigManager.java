package me.serafin.slogin.managers;

import lombok.Getter;
import me.serafin.slogin.SLogin;
import me.serafin.slogin.utils.FileLoader;
import me.serafin.slogin.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

@Getter
public final class ConfigManager {

    private final File file;

    private boolean LANGUAGE_AUTO;
    private String LANGUAGE_DEFAULT;

    private String DATATYPE;
    private String MYSQL_HOST;
    private String MYSQL_PORT;
    private String MYSQL_USER;
    private String MYSQL_PASS;
    private String MYSQL_DATABASE;
    private String MYSQL_PROPERTIES;

    private int MAX_ACCOUNTS_PER_IP;
    private boolean MESSAGES_CHAT_MESSAGES;
    private boolean MESSAGES_TITLE_MESSAGES;
    private List<String> ALLOWED_COMMANDS;
    private int PASSWORD_MIN_LENGTH;
    private int PASSWORD_MAX_LENGTH;
    private boolean EMAIL_NOTIFICATION;
    private int LOGIN_TIMEOUT;

    private boolean CAPTCHA_ON_REGISTER;
    private boolean CAPTCHA_ON_LOGIN;
    private boolean KICK_ON_WRONG_PASSWORD;

    ///////////////////////////////////////////

    public ConfigManager() {
        // Load defaults
        file = new File(SLogin.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            SLogin.getInstance().saveResource("config.yml", false);
        }

        loadConfig();
        SLogin.getInstance().getLogger().info("Loaded config file");
    }

    public void loadConfig() {
        FileLoader.matchConfig(file);
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        // Load settings
        this.LANGUAGE_AUTO = configuration.getBoolean("Language.Auto");
        this.LANGUAGE_DEFAULT = configuration.getString("Language.Default");

        this.DATATYPE = configuration.getString("DataType");
        this.MYSQL_HOST = configuration.getString("MySQL.Host");
        this.MYSQL_PORT = configuration.getString("MySQL.Port");
        this.MYSQL_USER = configuration.getString("MySQL.User");
        this.MYSQL_PASS = configuration.getString("MySQL.Pass");
        this.MYSQL_DATABASE = configuration.getString("MySQL.DataBase");
        this.MYSQL_PROPERTIES = configuration.getString("MySQL.Properties");

        this.MAX_ACCOUNTS_PER_IP = configuration.getInt("MaxAccountsPerIP");

        this.MESSAGES_CHAT_MESSAGES = configuration.getBoolean("Messages.ChatMessages");
        this.MESSAGES_TITLE_MESSAGES = Utils.isCompatible(Utils.getServerVersion(), "1.11") && configuration.getBoolean("Messages.TitleMessages");

        this.ALLOWED_COMMANDS = configuration.getStringList("AllowedCommands");

        this.PASSWORD_MIN_LENGTH = configuration.getInt("Password.min-length");
        this.PASSWORD_MAX_LENGTH = configuration.getInt("Password.max-length");

        this.EMAIL_NOTIFICATION = configuration.getBoolean("EmailNotification");

        this.LOGIN_TIMEOUT = configuration.getInt("LoginTimeout");

        this.CAPTCHA_ON_REGISTER = configuration.getBoolean("Captcha.OnRegister");
        this.CAPTCHA_ON_LOGIN = configuration.getBoolean("Captcha.OnLogin");

        this.KICK_ON_WRONG_PASSWORD = configuration.getBoolean("KickOnWrongPassword");
    }

    /**
     * Reloading
     */
    public void reloadConfig() {
        loadConfig();
        SLogin.getInstance().getLogger().info("Reloaded config file");
    }

}
