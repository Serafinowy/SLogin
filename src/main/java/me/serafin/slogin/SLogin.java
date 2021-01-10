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

    public static SLogin instance;
    private DataBase dataBase;
    public ConfigManager configManager;
    public LangManager langManager;
    public LoginManager loginManager;

    public LoginTimeoutManager loginTimeoutManager;
    public CaptchaManager captchaManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager();
        this.langManager = new LangManager( configManager.LANG);

        if(!setupDatabase()) {
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
        for(Player online : Bukkit.getOnlinePlayers())
            loginManager.playerJoin(online);
    }

    @Override
    public void onDisable() {
        if (dataBase == null)
            return;

        try {
            dataBase.closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean setupDatabase() {
        if(configManager.DATATYPE.equals("MYSQL")) {
            this.dataBase = new MySQL(configManager);
        } else {
            this.dataBase = new SQLite(new File(getDataFolder(), "database.db"));
        }

        try {
            dataBase.openConnection();
            dataBase.update("CREATE TABLE IF NOT EXISTS `seralogin`" +
                    "(`name` TEXT NOT NULL, " +
                    "`password` VARCHAR(255) NOT NULL, " +
                    "`registerIP` TEXT NOT NULL, " +
                    "`registerDate` BIGINT NOT NULL, " +
                    "`lastLoginIP` TEXT NOT NULL, " +
                    "`lastLoginDate` BIGINT NOT NULL)");
            Bukkit.getLogger().info("Connected to the database");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setupListeners() {
        getServer().getPluginManager().registerEvents(new PlayerActionListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    private void setupCommands() {
        getCommand("register").setExecutor(new RegisterCommand());
        getCommand("login").setExecutor(new LoginCommand());
        getCommand("changepassword").setExecutor(new ChangePasswordCommand());

        getCommand("slogin").setExecutor(new SLoginCommand());
    }

    private void checkVersion() {
        String latestVersion = Utils.getLatestVersion();
        if(latestVersion != null) {
            if(!Utils.isCorrectVersion(getDescription().getVersion(), latestVersion)) {
                getLogger().warning("New plugin version is available " + latestVersion);
                getLogger().warning("Download from https://www.spigotmc.org/resources/slogin.87073/");
            }
        }
    }

    public static SLogin getInstance() {
        return instance;
    }
}
