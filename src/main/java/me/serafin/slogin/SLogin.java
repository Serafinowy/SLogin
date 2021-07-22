package me.serafin.slogin;

import lombok.Getter;
import me.serafin.slogin.commands.ChangePasswordCommand;
import me.serafin.slogin.commands.EmailCommand;
import me.serafin.slogin.commands.LoginCommand;
import me.serafin.slogin.commands.RegisterCommand;
import me.serafin.slogin.commands.admin.SLoginCommand;
import me.serafin.slogin.listeners.PlayerActionListener;
import me.serafin.slogin.listeners.PlayerJoinListener;
import me.serafin.slogin.managers.*;
import me.serafin.slogin.utils.LoggerFilter;
import me.serafin.slogin.utils.Metrics;
import me.serafin.slogin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class SLogin extends JavaPlugin {

    @Getter
    private static SLogin instance;
    private Metrics metrics;

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

    @Override
    public void onEnable() {
        instance = this;
        this.metrics = new Metrics(this, 12160);

        this.configManager = new ConfigManager();
        this.configManager.loadConfig();
        this.langManager = new LangManager();
        this.langManager.loadLanguages();

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

    }

    @Override
    public void onDisable() {
        accountManager.closeDatabase();
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
