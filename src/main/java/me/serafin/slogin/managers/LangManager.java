package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.objects.Lang;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Logger;

public final class LangManager {

    private final ConfigManager config;
    private final Logger logger;

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
            File file = new File(SLogin.getInstance().getDataFolder(), "translations/" + locale + ".properties");

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
            SLogin.getInstance().getPluginLoader().disablePlugin(SLogin.getInstance());
        }
        if (TRANSLATIONS.get(config.LANGUAGE_DEFAULT.toLowerCase()) == null) {
            logger.severe("Cannot load default translation! SLogin has been disabled!");
            SLogin.getInstance().getPluginLoader().disablePlugin(SLogin.getInstance());
        }
    }

    /**
     * Load all languages file from plugin folder
     */
    public void loadLanguages() {
        File dataFolder = SLogin.getInstance().getDataFolder();
        File translationsFolder = new File(dataFolder, "translations");

        File[] files = translationsFolder.listFiles();
        if (!translationsFolder.exists() || files == null || files.length == 0) {
            translationsFolder.mkdir();
            loadDefaults(translationsFolder);
        }

        for (File file : Objects.requireNonNull(translationsFolder.listFiles())) {
            if (file.getName().endsWith(".properties") && file.isFile()) {
                registerLang(file.getName().replace(".properties", ""));
            }
        }

        checkLanguages();
    }

    /**
     * Copy default lang files from plugin to the plugin folder
     */
    private final Set<String> defaultLangSet = new HashSet<>(Arrays.asList("en_US", "pl_PL"));

    private void loadDefaults(File translationsFolder) {
        for (String lang : defaultLangSet) {
            try (InputStream is = SLogin.getInstance().getResource( "translations/" + lang + ".properties")) {
                File langFile = new File(translationsFolder, lang + ".properties");
                assert is != null;
                Files.copy(is, langFile.toPath());
                logger.warning("Created " + lang + " lang file!");
            } catch (IOException e) {
                e.printStackTrace();
                logger.severe("Cannot create " + lang + " lang file!");
            }
        }
    }

}
