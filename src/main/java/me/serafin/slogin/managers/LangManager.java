package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.objects.Lang;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

public final class LangManager {

    private final ConfigManager configManager;
    private final HashMap<String, Lang> TRANSLATIONS = new HashMap<>();

    public LangManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public Lang getLang(String locale) {
        if (!configManager.LANGUAGE_AUTO) locale = configManager.LANGUAGE_DEFAULT;

        assert locale != null;
        if (locale.equalsIgnoreCase("default")) locale = configManager.LANGUAGE_DEFAULT;

        return TRANSLATIONS.getOrDefault(locale, TRANSLATIONS.get("en_UK"));
    }
    public boolean registerLang(String locale) {
        try {
            FileInputStream fis = new FileInputStream(new File(SLogin.getInstance().getDataFolder(), locale + ".properties"));
            Properties properties = new Properties();
            properties.load(fis);
            TRANSLATIONS.put(locale, new Lang(properties));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
