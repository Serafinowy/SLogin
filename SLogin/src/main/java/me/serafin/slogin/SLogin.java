package me.serafin.slogin;

import lombok.Getter;
import me.serafin.slogin.commands.ChangePasswordCommand;
import me.serafin.slogin.commands.EmailCommand;
import me.serafin.slogin.commands.LoginCommand;
import me.serafin.slogin.commands.RegisterCommand;
import me.serafin.slogin.commands.admin.SLoginCommand;
import me.serafin.slogin.fastlogin.FastLoginHook;
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
        this.metrics = new Metrics(this, 12160);

        this.configManager = new ConfigManager(this);
        this.configManager.loadConfig();
        this.langManager = new LangManager(this);
        this.langManager.loadLanguages();

        this.accountManager = new AccountManager(this);
        this.loginManager = new LoginManager(this);

        setupListeners();
        setupCommands();

        this.loginTimeoutManager = new LoginTimeoutManager(this);
        this.captchaManager = new CaptchaManager(this);

        new LoggerFilter().registerFilter();

        if (getServer().getPluginManager().getPlugin("FastLogin") != null) {
            new FastLoginHook(this);
        }

        checkVersion();
        for (Player online : Bukkit.getOnlinePlayers()) {
            loginManager.playerJoin(online);
        }
    }

    @Override
    public void onDisable() {
        accountManager.closeDatabase();
    }

    private void setupListeners() {
        getServer().getPluginManager().registerEvents(new PlayerActionListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    @SuppressWarnings("all")
    private void setupCommands() {
        getCommand("register").setExecutor(new RegisterCommand(this));
        getCommand("login").setExecutor(new LoginCommand(this));
        getCommand("changepassword").setExecutor(new ChangePasswordCommand(this));
        getCommand("email").setExecutor(new EmailCommand(this));

        getCommand("slogin").setExecutor(new SLoginCommand(this));
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
