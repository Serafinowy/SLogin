package me.serafin.slogin;

import lombok.Getter;
import me.serafin.slogin.commands.ChangePasswordCommand;
import me.serafin.slogin.commands.EmailCommand;
import me.serafin.slogin.commands.LoginCommand;
import me.serafin.slogin.commands.RegisterCommand;
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

    @Getter
    private static SLogin instance;

    @Getter
    private ConfigManager configManager;
    @Getter
    private LangManager langManager;
    @Getter
    private AccountManager accountManager;
    @Getter
    private LoginManager loginManager;

    @Getter
    private LoginTimeoutManager loginTimeoutManager;
    @Getter
    private CaptchaManager captchaManager;

    @Getter
    private DataBase dataBase;

    @Override
    public void onEnable() {
        if (!setup())
            getLogger().severe("An error occurred while loading the plugin.");
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

    public boolean setup() {
        instance = this;
        this.configManager = new ConfigManager();
        this.langManager = new LangManager();
        this.langManager.loadLanguages();

        this.dataBase = setupDatabase();
        if (dataBase == null) {
            getLogger().severe("Error connecting to database! SLogin has been disabled!");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }

        this.accountManager = new AccountManager();
        this.loginManager = new LoginManager();

        setupListeners();
        setupCommands();

        this.loginTimeoutManager = new LoginTimeoutManager();
        this.captchaManager = new CaptchaManager();

        new LoggerFilter().registerFilter();

        checkVersion();
        for (Player online : Bukkit.getOnlinePlayers())
            loginManager.playerJoin(online);

        return true;
    }

    private DataBase setupDatabase() {
        DataBase dataBase = null;
        try {
            assert configManager.DATATYPE != null;
            if (configManager.DATATYPE.equals("MYSQL")) {
                dataBase = new MySQL(configManager);
            } else {
                dataBase = new SQLite(new File(getDataFolder(), "database.db"));
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
            getLogger().info("Connected to the " + configManager.DATATYPE + " database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataBase;
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
            if (!Utils.isCompatible(getDescription().getVersion(), latestVersion)) {
                getLogger().warning("New plugin version is available " + latestVersion);
                getLogger().warning("Download from https://www.spigotmc.org/resources/slogin.87073/");
            }
        }
    }
}
