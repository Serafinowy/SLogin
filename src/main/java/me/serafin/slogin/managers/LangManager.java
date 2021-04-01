package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class LangManager {

    public LangManager(String language){

        String lang_yml;
        if (language.equals("PL")) {
            lang_yml = "lang_PL.yml";
        } else {
            lang_yml = "lang_EN.yml";
        }

        // load Defaults
        File lang_file = new File(SLogin.getInstance().getDataFolder(), lang_yml);

        if(!lang_file.exists()){
            SLogin.getInstance().saveResource(lang_yml, true);
            Bukkit.getLogger().info("Created: " + lang_yml + " file");
        }
        Bukkit.getLogger().info("Loaded: " + lang_yml + " file");

        FileConfiguration lang = YamlConfiguration.loadConfiguration(lang_file);
        Utils.matchConfig(lang, lang_file);

        //load Messages
        this.onlyForPlayers = PREFIX + format(lang.getString("userMessages.onlyForPlayers"));
        this.wrongPassword = PREFIX + format(lang.getString("userMessages.wrongPassword"));
        this.alreadyLogged = PREFIX + format(lang.getString("userMessages.alreadyLogged"));
        this.mustLogin = PREFIX + format(lang.getString("userMessages.mustLogin"));
        this.maxAccounts = PREFIX + format(lang.getString("userMessages.maxAccounts"));

        ///////////////////////////////////////////

        this.loginInfo = PREFIX + format(lang.getString("userMessages.loginInfo"));
        this.loginCorrectUsage = PREFIX + format(lang.getString("userMessages.loginCorrectUsage"));
        this.loginSuccess = PREFIX + format(lang.getString("userMessages.loginSuccess"));

        this.loginTitle = format(lang.getString("userMessages.loginTitle"));
        this.loginSubTitle = format(lang.getString("userMessages.loginSubTitle"));

        this.loginSuccessTitle = format(lang.getString("userMessages.loginSuccessTitle"));
        this.loginSuccessSubTitle = format(lang.getString("userMessages.loginSuccessSubTitle"));

        ///////////////////////////////////////////

        this.notAllowedPasswordLength = PREFIX + format(lang.getString("userMessages.notAllowedPasswordLength"));
        this.differentPasswords = PREFIX + format(lang.getString("userMessages.differentPasswords"));
        this.alreadyRegistered = PREFIX + format(lang.getString("userMessages.alreadyRegistered"));
        this.notRegistered = PREFIX + format(lang.getString("userMessages.notRegistered"));

        this.registerInfo = PREFIX + format(lang.getString("userMessages.registerInfo"));
        this.registerCorrectUsage = PREFIX + format(lang.getString("userMessages.registerCorrectUsage"));
        this.registerSuccess = PREFIX + format(lang.getString("userMessages.registerSuccess"));

        this.registerTitle = format(lang.getString("userMessages.registerTitle"));
        this.registerSubTitle = format(lang.getString("userMessages.registerSubTitle"));

        this.registerSuccessTitle = format(lang.getString("userMessages.registerSuccessTitle"));
        this.registerSuccessSubTitle = format(lang.getString("userMessages.registerSuccessSubTitle"));

        ///////////////////////////////////////////

        this.changePassCorrectUsage = PREFIX + format(lang.getString("userMessages.changePassCorrectUsage"));
        this.changePassSuccess = PREFIX + format(lang.getString("userMessages.changePassSuccess"));

        ///////////////////////////////////////////

        this.emailInfo = PREFIX + format(lang.getString("userMessages.emailInfo"));
        this.emailCorrectUsage = PREFIX + format(lang.getString("userMessages.emailCorrectUsage"));
        this.emailChangeSuccess = PREFIX + format(lang.getString("userMessages.emailChangeSuccess"));

        this.emailNotSet = PREFIX + format(lang.getString("userMessages.emailNotSet"));
        this.emailBadFormat = PREFIX + format(lang.getString("userMessages.emailBadFormat"));

        ///////////////////////////////////////////

        this.nullValue = format(lang.getString("adminMessages.nullValue"));
        this.userNotExists = PREFIX + format(lang.getString("adminMessages.userNotExists"));
        this.userIsNotOnline = PREFIX + format(lang.getString("adminMessages.userIsNotOnline"));

        this.commandListTitle = format(lang.getString("adminMessages.commandList.title"));
        this.commandListChatFormat = format(lang.getString("adminMessages.commandList.chatFormat"));
        this.commandListHoverFormat = format(lang.getString("adminMessages.commandList.hoverFormat"));

        this.playerInfoCorrectUsage = PREFIX + format(lang.getString("adminMessages.playerInfoCorrectUsage"));
        this.playerInfoMessage = format(lang.getString("adminMessages.playerInfoMessage"));

        this.changePassAdminCorrectUsage = PREFIX + format(lang.getString("adminMessages.changePassAdminCorrectUsage"));
        this.changePassAdminSuccess = PREFIX + format(lang.getString("adminMessages.changePassAdminSuccess"));

        this.forceLoginCorrectUsage = PREFIX + format(lang.getString("adminMessages.forceLoginCorrectUsage"));
        this.forceLoginSuccess = PREFIX + format(lang.getString("adminMessages.forceLoginSuccess"));
        this.forceLoginDeny = PREFIX + format(lang.getString("adminMessages.forceLoginDeny"));

        this.registerAdminCorrectUsage = PREFIX + format(lang.getString("adminMessages.registerAdminCorrectUsage"));
        this.registerAdminSuccess = PREFIX + format(lang.getString("adminMessages.registerAdminSuccess"));
        this.registerAdminDeny = PREFIX + format(lang.getString("adminMessages.registerAdminDeny"));

        this.unRegisterCorrectUsage = PREFIX + format(lang.getString("adminMessages.unRegisterCorrectUsage"));
        this.unRegisterSuccess = PREFIX + format(lang.getString("adminMessages.unRegisterSuccess"));

        ///////////////////////////////////////////

        this.loginTimeoutKick = format(lang.getString("loginTimeoutKick"));

        this.captcha_guiName = format(lang.getString("captchaMessages.guiName"));
        this.captcha_kickMessage = format(lang.getString("captchaMessages.kickMessage"));

        this.wrongPassword_kickMessage = format(lang.getString("wrongPassword.kickMessage"));
    }

    ///////////////////////////////////////////

    public final String PREFIX = format("&e[SLogin] &r");

    public final String onlyForPlayers;
    public final String wrongPassword;
    public final String alreadyLogged;
    public final String mustLogin;
    public final String maxAccounts;

    ///////////////////////////////////////////

    public final String loginInfo;
    public final String loginCorrectUsage;
    public final String loginSuccess;

    public final String loginTitle;
    public final String loginSubTitle;

    public final String loginSuccessTitle;
    public final String loginSuccessSubTitle;

    ///////////////////////////////////////////

    public final String notAllowedPasswordLength;
    public final String differentPasswords;
    public final String alreadyRegistered;
    public final String notRegistered;

    public final String registerInfo;
    public final String registerCorrectUsage;
    public final String registerSuccess;

    public final String registerTitle;
    public final String registerSubTitle;

    public final String registerSuccessTitle;
    public final String registerSuccessSubTitle;

    ///////////////////////////////////////////

    public final String changePassCorrectUsage;
    public final String changePassSuccess;

    ///////////////////////////////////////////

    public final String emailInfo;
    public final String emailCorrectUsage;
    public final String emailChangeSuccess;

    public final String emailNotSet;
    public final String emailBadFormat;

    ///////////////////////////////////////////

    public final String nullValue;
    public final String userNotExists;
    public final String userIsNotOnline;

    public final String commandListTitle;
    public final String commandListChatFormat;
    public final String commandListHoverFormat;

    public final String playerInfoCorrectUsage;
    public final String playerInfoMessage;

    public final String changePassAdminCorrectUsage;
    public final String changePassAdminSuccess;

    public final String forceLoginCorrectUsage;
    public final String forceLoginSuccess;
    public final String forceLoginDeny;

    public final String registerAdminCorrectUsage;
    public final String registerAdminSuccess;
    public final String registerAdminDeny;

    public final String unRegisterCorrectUsage;
    public final String unRegisterSuccess;

    ///////////////////////////////////////////

    public final String loginTimeoutKick;

    public final String captcha_guiName;
    public final String captcha_kickMessage;

    ///////////////////////////////////////////

    public final String wrongPassword_kickMessage;

    private static String format(String s){
        return (s==null) ? "<null>" : ChatColor.translateAlternateColorCodes('&', s);
    }

}
