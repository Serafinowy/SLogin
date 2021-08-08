package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.models.Lang;
import me.serafin.slogin.utils.FileLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public final class LangManager {

    private final SLogin plugin = SLogin.getInstance();
    private final ConfigManager configManager = plugin.getConfigManager();
    private final Logger logger = plugin.getLogger();

    private final HashMap<String, Lang> TRANSLATIONS = new HashMap<>();

    /**
     * Getting specified Lang
     *
     * @param locale locale to get
     * @return Lang object with messages if locale is in translations map and
     * auto language is disabled, otherwise the default language.
     */
    public Lang getLang(String locale) {
        Lang lang = TRANSLATIONS.get(locale.toLowerCase());
        return (!configManager.getConfig().LANGUAGE_AUTO || lang == null) ? TRANSLATIONS.get(configManager.getConfig().LANGUAGE_DEFAULT.toLowerCase()) : lang;
    }

    /**
     * Loading translation file to translations map
     *
     * @param locale locale to load
     */
    private void registerLang(String locale) {
        File file = new File(plugin.getDataFolder(), "translations/" + locale + ".properties");

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)
        ) {
            Properties properties = new Properties();
            properties.load(isr);
            TRANSLATIONS.put(locale.toLowerCase(), new Lang(properties));

            logger.info("Loaded " + locale + " lang file!");
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.warning("Filed loading " + locale + " lang file!");
        }
    }

    /**
     * Checking translations map. If map is empty or does not contain
     * default language, the entire plugin is disable.
     */
    private void checkLanguages() {
        if (TRANSLATIONS.size() == 0) {
            logger.severe("Cannot load any translations! SLogin has been disabled!");
            plugin.getPluginLoader().disablePlugin(plugin);
        }
        if (TRANSLATIONS.get(configManager.getConfig().LANGUAGE_DEFAULT.toLowerCase()) == null) {
            logger.severe("Cannot load default translation! SLogin has been disabled!");
            plugin.getPluginLoader().disablePlugin(plugin);
        }
    }

    /**
     * Load all translations files from plugin translations folder
     */
    public void loadLanguages() {
        final File translationsFolder = new File(plugin.getDataFolder(), "translations");

        File[] files = translationsFolder.listFiles();
        if (!translationsFolder.exists() || files == null || files.length == 0) {
            if (translationsFolder.mkdir()) {
                logger.info("Created translations folder!");
            }
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
        List<String> defaultLanguagesFiles = FileLoader.getAllResourcesIn("translations");

        if (defaultLanguagesFiles == null) {
            logger.severe("List of translations in jar is empty!");
            return;
        }

        for (String name : defaultLanguagesFiles) {
            try {
                plugin.saveResource(name, false);
                logger.warning("Created " + name + " language file");
            } catch (Exception e) {
                e.printStackTrace();
                logger.severe("Error while copying " + name + " language file!");
            }
        }
    }

    /**
     * Reloads all languages.
     */
    public void reloadLanguages() {
        loadLanguages();
        SLogin.getInstance().getLogger().info("Reloaded languages files");
    }

}
