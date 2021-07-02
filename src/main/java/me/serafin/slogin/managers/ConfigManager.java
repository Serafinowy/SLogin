package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.models.Config;
import me.serafin.slogin.utils.FileLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class ConfigManager {

    private final File file;
    private Config config;

    ///////////////////////////////////////////

    public ConfigManager() {
        file = new File(SLogin.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            SLogin.getInstance().saveResource("config.yml", false);
        }
    }

    /**
     * Loading config file from plugin's folder
     */
    public void loadConfig() {
        FileLoader.matchConfig(file);
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        this.config = new Config(configuration);
        SLogin.getInstance().getLogger().info("Loaded config file");
    }

    /**
     * Reloading config data
     */
    public void reloadConfig() {
        loadConfig();
    }

    /**
     * Getter for config object
     * @return config object
     */
    public Config getConfig() {
        return config;
    }
}
