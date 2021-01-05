package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class ConfigManager {

    private final SLogin plugin;
    private FileConfiguration configuration;

    public ConfigManager(SLogin plugin){
        this.plugin = plugin;

        loadDefault();
        loadSettings();
    }

    ///////////////////////////////////////////

    public String LANG;
    public String DATATYPE;

    public String MYSQL_HOST;
    public String MYSQL_PORT;
    public String MYSQL_USER;
    public String MYSQL_PASS;
    public String MYSQL_DATABASE;

    public int MAX_ACCOUNTS_PER_IP;

    public boolean MESSAGES_CHAT_MESSAGES;
    public boolean MESSAGES_TITLE_MESSAGES;

    public List<String> ALLOWED_COMMANDS;

    public int PASSWORD_MIN_LENGTH;
    public int PASSWORD_MAX_LENGTH;

    ///////////////////////////////////////////

    public boolean CAPTCHA_ENABLED;
    public boolean KICK_ON_WRONG_PASSWORD;

    ///////////////////////////////////////////

    /**
     * Loading default settings (from plugin)
     */
    private void loadDefault(){
        File file = new File(plugin.getDataFolder(), "config.yml");

        if(!file.exists()){
            plugin.saveResource("config.yml", false);
        }
        configuration = YamlConfiguration.loadConfiguration(file);

        Utils.matchConfig(configuration, file);
        Bukkit.getLogger().info("Loaded: config.yml file");
    }

    /**
     * Loading settings
     */
    private void loadSettings(){
        this.LANG = configuration.getString("Language");
        this.DATATYPE = configuration.getString("DataType");

        this.MYSQL_HOST = configuration.getString("MySQL.Host");
        this.MYSQL_PORT = configuration.getString("MySQL.Port");
        this.MYSQL_USER = configuration.getString("MySQL.User");
        this.MYSQL_PASS = configuration.getString("MySQL.Pass");
        this.MYSQL_DATABASE = configuration.getString("MySQL.DataBase");

        this.MAX_ACCOUNTS_PER_IP = configuration.getInt("MaxAccountsPerIP");

        this.MESSAGES_CHAT_MESSAGES = configuration.getBoolean("Messages.ChatMessages");
        this.MESSAGES_TITLE_MESSAGES = Utils.isCorrectVersion(Utils.getServerVersion(), "1.11") && configuration.getBoolean("Messages.TitleMessages");

        this.ALLOWED_COMMANDS = configuration.getStringList("AllowedCommands");

        this.PASSWORD_MIN_LENGTH = configuration.getInt("Password.min-length");
        this.PASSWORD_MAX_LENGTH = configuration.getInt("Password.max-length");

        this.CAPTCHA_ENABLED = configuration.getBoolean("CaptchaEnabled");
        this.KICK_ON_WRONG_PASSWORD = configuration.getBoolean("KickOnWrongPassword");
    }
}
