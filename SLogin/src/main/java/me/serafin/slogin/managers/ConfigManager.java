package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.models.Config;
import me.serafin.slogin.utils.FileLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class ConfigManager {

    private final SLogin plugin;
    private final File file;
    private Config config;

    ///////////////////////////////////////////

    public ConfigManager(SLogin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "config.yml");

        if (!file.exists()) {
            plugin.saveResource("config.yml", false);
        }
    }

    /**
     * Loading config file from plugin's folder
     */
    public void loadConfig() {
        FileLoader.matchConfig(plugin, file);
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        this.config = new Config(configuration);
        plugin.getLogger().info("Loaded config file");
    }

    /**
     * Reloading config data
     */
    public void reloadConfig() {
        loadConfig();
    }

    /**
     * Getter for config object
     *
     * @return config object
     */
    public Config getConfig() {
        return config;
    }
}
