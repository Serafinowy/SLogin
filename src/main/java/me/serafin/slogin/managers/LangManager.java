package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LangManager {

    private final SLogin plugin;
    private final String lang_yml;

    public LangManager(SLogin plugin, String language){
        this.plugin = plugin;

        if ("PL".equalsIgnoreCase(language)) {
            lang_yml = "lang_PL.yml";
        } else {
            lang_yml = "lang_EN.yml";
        }

        loadDefault();
        setupMessages();
    }

    ///////////////////////////////////////////

    public final String PREFIX = Utils.format("&e[SLogin] &r");

    public String onlyForPlayers;
    public String wrongPassword;
    public String alreadyLogged;
    public String mustLogin;
    public String maxAccounts;

    ///////////////////////////////////////////

    public String loginInfo;
    public String loginCorrectUsage;
    public String loginSuccess;

    public String loginTitle;
    public String loginSubTitle;

    public String loginSuccessTitle;
    public String loginSuccessSubTitle;

    ///////////////////////////////////////////

    public String notAllowedPasswordLength;
    public String differentPasswords;
    public String alreadyRegistered;
    public String notRegistered;

    public String registerInfo;
    public String registerCorrectUsage;
    public String registerSuccess;

    public String registerTitle;
    public String registerSubTitle;

    public String registerSuccessTitle;
    public String registerSuccessSubTitle;

    ///////////////////////////////////////////

    public String changePassCorrectUsage;
    public String changePassSuccess;

    ///////////////////////////////////////////

    public String userNotExists;
    public String userIsNotOnline;

    public String playerInfoCorrectUsage;

    public String changePassAdminCorrectUsage;
    public String changePassAdminSuccess;

    public String forceLoginCorrectUsage;
    public String forceLoginSuccess;
    public String forceLoginDeny;

    public String registerAdminCorrectUsage;
    public String registerAdminSuccess;
    public String registerAdminDeny;

    public String unRegisterCorrectUsage;
    public String unRegisterSuccess;

    ///////////////////////////////////////////

    public String captcha_guiName;
    public String captcha_kickMessage;

    ///////////////////////////////////////////

    /**
     * Loading default language file (from plugin)
     */
    FileConfiguration lang;
    private void loadDefault() {
        File lang_file = new File(plugin.getDataFolder(), lang_yml);

        if(!lang_file.exists()){
            plugin.saveResource(lang_yml, true);
            Bukkit.getLogger().info("Created: " + lang_yml + " file");
        }
        Bukkit.getLogger().info("Loaded: " + lang_yml + " file");
        lang = YamlConfiguration.loadConfiguration(lang_file);
        Utils.matchConfig(lang, lang_file);
    }

    /**
     * Loading messages
     */
    private void setupMessages(){
        onlyForPlayers = PREFIX + Utils.format(lang.getString("userMessages.onlyForPlayers"));
        wrongPassword = PREFIX + Utils.format(lang.getString("userMessages.wrongPassword"));
        alreadyLogged = PREFIX + Utils.format(lang.getString("userMessages.alreadyLogged"));
        mustLogin = PREFIX + Utils.format(lang.getString("userMessages.mustLogin"));
        maxAccounts = PREFIX + Utils.format(lang.getString("userMessages.maxAccounts"));

        ///////////////////////////////////////////

        loginInfo = PREFIX + Utils.format(lang.getString("userMessages.loginInfo"));
        loginCorrectUsage = PREFIX + Utils.format(lang.getString("userMessages.loginCorrectUsage"));
        loginSuccess = PREFIX + Utils.format(lang.getString("userMessages.loginSuccess"));

        loginTitle = Utils.format(lang.getString("userMessages.loginTitle"));
        loginSubTitle = Utils.format(lang.getString("userMessages.loginSubTitle"));

        loginSuccessTitle = Utils.format(lang.getString("userMessages.loginSuccessTitle"));
        loginSuccessSubTitle = Utils.format(lang.getString("userMessages.loginSuccessSubTitle"));

        ///////////////////////////////////////////

        notAllowedPasswordLength = PREFIX + Utils.format(lang.getString("userMessages.notAllowedPasswordLength"));
        differentPasswords = PREFIX + Utils.format(lang.getString("userMessages.differentPasswords"));
        alreadyRegistered = PREFIX + Utils.format(lang.getString("userMessages.alreadyRegistered"));
        notRegistered = PREFIX + Utils.format(lang.getString("userMessages.notRegistered"));

        registerInfo = PREFIX + Utils.format(lang.getString("userMessages.registerInfo"));
        registerCorrectUsage = PREFIX + Utils.format(lang.getString("userMessages.registerCorrectUsage"));
        registerSuccess = PREFIX + Utils.format(lang.getString("userMessages.registerSuccess"));

        registerTitle = Utils.format(lang.getString("userMessages.registerTitle"));
        registerSubTitle = Utils.format(lang.getString("userMessages.registerSubTitle"));

        registerSuccessTitle = Utils.format(lang.getString("userMessages.registerSuccessTitle"));
        registerSuccessSubTitle = Utils.format(lang.getString("userMessages.registerSuccessSubTitle"));

        ///////////////////////////////////////////

        changePassCorrectUsage = PREFIX + Utils.format(lang.getString("userMessages.changePassCorrectUsage"));
        changePassSuccess = PREFIX + Utils.format(lang.getString("userMessages.changePassSuccess"));

        ///////////////////////////////////////////

        userNotExists = PREFIX + Utils.format(lang.getString("adminMessages.userNotExists"));
        userIsNotOnline = PREFIX + Utils.format(lang.getString("adminMessages.userIsNotOnline"));

        playerInfoCorrectUsage = PREFIX + Utils.format(lang.getString("adminMessages.playerInfoCorrectUsage"));

        changePassAdminCorrectUsage = PREFIX + Utils.format(lang.getString("adminMessages.changePassAdminCorrectUsage"));
        changePassAdminSuccess = PREFIX + Utils.format(lang.getString("adminMessages.changePassAdminSuccess"));

        forceLoginCorrectUsage = PREFIX + Utils.format(lang.getString("adminMessages.forceLoginCorrectUsage"));
        forceLoginSuccess = PREFIX + Utils.format(lang.getString("adminMessages.forceLoginSuccess"));
        forceLoginDeny = PREFIX + Utils.format(lang.getString("adminMessages.forceLoginDeny"));

        registerAdminCorrectUsage = PREFIX + Utils.format(lang.getString("adminMessages.registerAdminCorrectUsage"));
        registerAdminSuccess = PREFIX + Utils.format(lang.getString("adminMessages.registerAdminSuccess"));
        registerAdminDeny = PREFIX + Utils.format(lang.getString("adminMessages.registerAdminDeny"));

        unRegisterCorrectUsage = PREFIX + Utils.format(lang.getString("adminMessages.unRegisterCorrectUsage"));
        unRegisterSuccess = PREFIX + Utils.format(lang.getString("adminMessages.unRegisterSuccess"));

        ///////////////////////////////////////////

        captcha_guiName = Utils.format(lang.getString("captchaMessages.guiName"));
        captcha_kickMessage = Utils.format(lang.getString("captchaMessages.kickMessage"));
    }
}
