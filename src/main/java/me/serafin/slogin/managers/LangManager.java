package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.objects.Lang;
import me.serafin.slogin.utils.Utils;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public final class LangManager {

    private final SLogin plugin = SLogin.getInstance();
    private final ConfigManager config;
    private final Logger logger;

    private final File translationsFolder = new File(plugin.getDataFolder(), "translations");

    private final HashMap<String, Lang> TRANSLATIONS = new HashMap<>();

    public LangManager(ConfigManager config) {
        this.config = config;
        this.logger = SLogin.getInstance().getLogger();
    }

    /**
     * Gets lang from plugin settings
     *
     * @param locale lang locale
     * @return Lang object with messages
     */
    public Lang getLang(String locale) {
        if (!config.LANGUAGE_AUTO) locale = config.LANGUAGE_DEFAULT;
        if (TRANSLATIONS.get(locale.toLowerCase()) == null) locale = config.LANGUAGE_DEFAULT;
        return TRANSLATIONS.get(locale.toLowerCase());
    }

    /**
     * Add lang file into plugin setting
     *
     * @param locale lang locale
     */
    private void registerLang(String locale) {
        try {
            File file = new File(plugin.getDataFolder(), "translations/" + locale + ".properties");

            Properties properties = new Properties();
            properties.load(new FileReader(file));
            TRANSLATIONS.put(locale.toLowerCase(), new Lang(properties));

            logger.info("Loaded " + locale + " lang file!");
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.warning("Filed loading " + locale + " lang file!");
        }
    }

    /**
     * Check lang files
     */
    private void checkLanguages() {
        if (TRANSLATIONS.size() == 0) {
            logger.severe("Cannot load any translations! SLogin has been disabled!");
            plugin.getPluginLoader().disablePlugin(plugin);
        }
        if (TRANSLATIONS.get(config.LANGUAGE_DEFAULT.toLowerCase()) == null) {
            logger.severe("Cannot load default translation! SLogin has been disabled!");
            plugin.getPluginLoader().disablePlugin(plugin);
        }
    }

    /**
     * Load all languages file from plugin folder
     */
    public void loadLanguages() {
        File[] files = translationsFolder.listFiles();
        if (!translationsFolder.exists() || files == null || files.length == 0) {
            translationsFolder.mkdir();
            loadDefaults();
        }

        for (File file : Objects.requireNonNull(translationsFolder.listFiles())) {
            if (file.getName().endsWith(".properties") && file.isFile()) {
                registerLang(file.getName().replace(".properties", ""));
            }
        }

        checkLanguages();
    }

    /**
     * Load all languages from plugin JAR
     */
    private void loadDefaults() {
        List<String> defaultLanguagesFiles = Utils.getAllResourcesIn("translations");

        if (defaultLanguagesFiles == null) {
            logger.severe("Cannot create copy language files from jar!");
            return;
        }

        defaultLanguagesFiles.forEach(name -> {
            plugin.saveResource(name, false);
            logger.warning("Created " + name + " language file");
        });
    }

}
