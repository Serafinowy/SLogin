package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public final class ConfigManager {

    public ConfigManager(){

        // load Defaults
        File file = new File(SLogin.getInstance().getDataFolder(), "config.yml");

        if(!file.exists()){
            SLogin.getInstance().saveResource("config.yml", false);
        }
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        Utils.matchConfig(configuration, file);
        Bukkit.getLogger().info("Loaded: config.yml file");

        // load Settings
        this.LANG = configuration.getString("Language");
        this.DATATYPE = configuration.getString("DataType");

        this.MYSQL_HOST = configuration.getString("MySQL.Host");
        this.MYSQL_PORT = configuration.getString("MySQL.Port");
        this.MYSQL_USER = configuration.getString("MySQL.User");
        this.MYSQL_PASS = configuration.getString("MySQL.Pass");
        this.MYSQL_DATABASE = configuration.getString("MySQL.DataBase");
        this.MYSQL_PROPERTIES = configuration.getString("MySQL.Properties");

        this.MAX_ACCOUNTS_PER_IP = configuration.getInt("MaxAccountsPerIP");

        this.MESSAGES_CHAT_MESSAGES = configuration.getBoolean("Messages.ChatMessages");
        this.MESSAGES_TITLE_MESSAGES = Utils.isCorrectVersion(Utils.getServerVersion(), "1.11") && configuration.getBoolean("Messages.TitleMessages");

        this.ALLOWED_COMMANDS = configuration.getStringList("AllowedCommands");

        this.PASSWORD_MIN_LENGTH = configuration.getInt("Password.min-length");
        this.PASSWORD_MAX_LENGTH = configuration.getInt("Password.max-length");

        this.EMAIL_NOTIFICATION = configuration.getBoolean("EmailNotification");

        this.LOGIN_TIMEOUT = configuration.getInt("LoginTimeout");

        this.CAPTCHA_ON_REGISTER = configuration.getBoolean("Captcha.OnRegister");
        this.CAPTCHA_ON_LOGIN = configuration.getBoolean("Captcha.OnLogin");

        this.KICK_ON_WRONG_PASSWORD = configuration.getBoolean("KickOnWrongPassword");
    }

    ///////////////////////////////////////////

    public final String LANG;
    public final String DATATYPE;

    public final String MYSQL_HOST;
    public final String MYSQL_PORT;
    public final String MYSQL_USER;
    public final String MYSQL_PASS;
    public final String MYSQL_DATABASE;
    public final String MYSQL_PROPERTIES;

    public final int MAX_ACCOUNTS_PER_IP;

    public final boolean MESSAGES_CHAT_MESSAGES;
    public final boolean MESSAGES_TITLE_MESSAGES;

    public final List<String> ALLOWED_COMMANDS;

    public final int PASSWORD_MIN_LENGTH;
    public final int PASSWORD_MAX_LENGTH;

    public final boolean EMAIL_NOTIFICATION;

    ///////////////////////////////////////////

    public final int LOGIN_TIMEOUT;

    public final boolean CAPTCHA_ON_REGISTER;
    public final boolean CAPTCHA_ON_LOGIN;

    public final boolean KICK_ON_WRONG_PASSWORD;
}
