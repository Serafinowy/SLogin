package me.serafin.slogin.models;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {

    public final boolean LANGUAGE_AUTO;
    public final String LANGUAGE_DEFAULT;

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
    public final int LOGIN_TIMEOUT;

    public final boolean CAPTCHA_ON_REGISTER;
    public final boolean CAPTCHA_ON_LOGIN;
    public final boolean KICK_ON_WRONG_PASSWORD;

    ///////////////////////////////////////////

    public Config(FileConfiguration configuration) {
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
        this.MESSAGES_TITLE_MESSAGES = configuration.getBoolean("Messages.TitleMessages");

        this.ALLOWED_COMMANDS = configuration.getStringList("AllowedCommands");

        this.PASSWORD_MIN_LENGTH = configuration.getInt("Password.min-length");
        this.PASSWORD_MAX_LENGTH = configuration.getInt("Password.max-length");

        this.EMAIL_NOTIFICATION = configuration.getBoolean("EmailNotification");

        this.LOGIN_TIMEOUT = configuration.getInt("LoginTimeout");

        this.CAPTCHA_ON_REGISTER = configuration.getBoolean("Captcha.OnRegister");
        this.CAPTCHA_ON_LOGIN = configuration.getBoolean("Captcha.OnLogin");

        this.KICK_ON_WRONG_PASSWORD = configuration.getBoolean("KickOnWrongPassword");
    }

}
