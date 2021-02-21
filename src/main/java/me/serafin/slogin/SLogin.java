package me.serafin.slogin;

import me.serafin.slogin.commands.*;
import me.serafin.slogin.commands.admin.SLoginCommand;
import me.serafin.slogin.database.DataBase;
import me.serafin.slogin.database.MySQL;
import me.serafin.slogin.database.SQLite;
import me.serafin.slogin.listeners.PlayerActionListener;
import me.serafin.slogin.listeners.PlayerJoinListener;
import me.serafin.slogin.managers.*;
import me.serafin.slogin.utils.LoggerFilter;
import me.serafin.slogin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public final class SLogin extends JavaPlugin {

    private static SLogin instance;
    private DataBase dataBase;
    private ConfigManager configManager;
    private LangManager langManager;
    private LoginManager loginManager;

    private LoginTimeoutManager loginTimeoutManager;
    private CaptchaManager captchaManager;

    @Override
    public void onEnable() {
        instance = this;
        this.configManager = new ConfigManager();

        assert configManager.LANG != null;
        this.langManager = new LangManager(configManager.LANG);

        if (!setupDatabase()) {
            Bukkit.getLogger().severe("Failed to connect database. Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.loginManager = new LoginManager(dataBase);

        setupListeners();
        setupCommands();

        this.loginTimeoutManager = new LoginTimeoutManager();
        this.captchaManager = new CaptchaManager();

        new LoggerFilter().registerFilter();

        checkVersion();
        for (Player online : Bukkit.getOnlinePlayers())
            loginManager.playerJoin(online);
    }

    @Override
    public void onDisable() {
        if (dataBase != null) {
            try {
                dataBase.closeConnection();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean setupDatabase() {
        assert configManager.DATATYPE != null;
        if (configManager.DATATYPE.equals("MYSQL")) {
            this.dataBase = new MySQL(configManager);
        } else {
            this.dataBase = new SQLite(new File(getDataFolder(), "database.db"));
        }

        try {
            dataBase.openConnection();
            dataBase.update("CREATE TABLE IF NOT EXISTS `slogin_accounts`" +
                    "(`name` VARCHAR(255) NOT NULL PRIMARY KEY, " +
                    "`password` VARCHAR(255) NOT NULL, " +
                    "`email` VARCHAR(255) NULL, " +
                    "`registerIP` TEXT NOT NULL, " +
                    "`registerDate` BIGINT NOT NULL, " +
                    "`lastLoginIP` TEXT NOT NULL, " +
                    "`lastLoginDate` BIGINT NOT NULL)");
            Bukkit.getLogger().info("Connected to the " + configManager.DATATYPE + " database");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setupListeners() {
        getServer().getPluginManager().registerEvents(new PlayerActionListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @SuppressWarnings("all")
    private void setupCommands() {
        getCommand("register").setExecutor(new RegisterCommand());
        getCommand("login").setExecutor(new LoginCommand());
        getCommand("changepassword").setExecutor(new ChangePasswordCommand());
        getCommand("email").setExecutor(new EmailCommand());

        getCommand("slogin").setExecutor(new SLoginCommand());
    }

    private void checkVersion() {
        String latestVersion = Utils.getLatestVersion();
        if (latestVersion != null) {
            if (!Utils.isCorrectVersion(getDescription().getVersion(), latestVersion)) {
                getLogger().warning("New plugin version is available " + latestVersion);
                getLogger().warning("Download from https://www.spigotmc.org/resources/slogin.87073/");
            }
        }
    }

    public static SLogin getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LangManager getLangManager() {
        return langManager;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public LoginTimeoutManager getLoginTimeoutManager() {
        return loginTimeoutManager;
    }

    public CaptchaManager getCaptchaManager() {
        return captchaManager;
    }
}
