package me.serafin.slogin.objects;

import org.bukkit.ChatColor;

import java.util.Properties;

public class Lang {

    public final String PREFIX = format("&e[SLogin] &r");

    public Lang(Properties properties) {

        misc_onlyForPlayers = PREFIX + format(properties.getProperty("slogin.misc.onlyForPlayers"));
        misc_nullValue = format(properties.getProperty("slogin.misc.nullValue"));

        ///////////////////////////////////////////

        system_alreadyLogged = PREFIX + format(properties.getProperty("slogin.system.alreadyLogged"));
        system_mustLogin = PREFIX + format(properties.getProperty("slogin.system.mustLogin"));
        system_maxAccounts = PREFIX + format(properties.getProperty("slogin.system.maxAccounts"));
        system_wrongPassword_chatMessage = PREFIX + format(properties.getProperty("slogin.system.wrongPassword.chatMessage"));
        system_wrongPassword_kickMessage = PREFIX + format(properties.getProperty("slogin.system.wrongPassword.kickMessage"));

        system_notAllowedPasswordLength = PREFIX + format(properties.getProperty("slogin.system.notAllowedPasswordLength"));
        system_differentPasswords = PREFIX + format(properties.getProperty("slogin.system.differentPasswords"));
        system_alreadyRegistered = PREFIX + format(properties.getProperty("slogin.system.alreadyRegistered"));
        system_notRegistered = PREFIX + format(properties.getProperty("slogin.system.notRegistered"));

        ///////////////////////////////////////////

        auth_login_info = PREFIX + format(properties.getProperty("slogin.auth.login.info"));
        auth_login_correctUsage = PREFIX + format(properties.getProperty("slogin.auth.login.correctUsage"));
        auth_login_success = PREFIX + format(properties.getProperty("slogin.auth.login.success"));
        auth_login_title = format(properties.getProperty("slogin.auth.login.title"));
        auth_login_subTitle = format(properties.getProperty("slogin.auth.login.subTitle"));
        auth_login_successTitle = format(properties.getProperty("slogin.auth.login.successTitle"));
        auth_login_successSubTitle = format(properties.getProperty("slogin.auth.login.successSubTitle"));
        auth_login_timeoutKick = format(properties.getProperty("slogin.auth.login.timeoutKick"));

        ///////////////////////////////////////////

        auth_register_info = PREFIX + format(properties.getProperty("slogin.auth.register.info"));
        auth_register_correctUsage = PREFIX + format(properties.getProperty("slogin.auth.register.correctUsage"));
        auth_register_success = PREFIX + format(properties.getProperty("slogin.auth.register.success"));
        auth_register_title = format(properties.getProperty("slogin.auth.register.title"));
        auth_register_subTitle = format(properties.getProperty("slogin.auth.register.subTitle"));
        auth_register_successTitle = format(properties.getProperty("slogin.auth.register.successTitle"));
        auth_register_successSubTitle = format(properties.getProperty("slogin.auth.register.successSubTitle"));

        ///////////////////////////////////////////

        auth_changePass_correctUsage = PREFIX + format(properties.getProperty("slogin.auth.changePass.correctUsage"));
        auth_changePass_success = PREFIX + format(properties.getProperty("slogin.auth.changePass.success"));

        ///////////////////////////////////////////

        auth_email_info = PREFIX + format(properties.getProperty("slogin.auth.email.info"));
        auth_email_correctUsage = PREFIX + format(properties.getProperty("slogin.auth.email.correctUsage"));
        auth_email_changeSuccess = PREFIX + format(properties.getProperty("slogin.auth.email.changeSuccess"));
        auth_email_notSet = PREFIX + format(properties.getProperty("slogin.auth.email.notSet"));
        auth_email_badFormat = PREFIX + format(properties.getProperty("slogin.auth.email.badFormat"));

        ///////////////////////////////////////////

        captcha_guiName = format(properties.getProperty("slogin.captcha.guiName"));
        captcha_kickMessage = format(properties.getProperty("slogin.captcha.kickMessage"));

        ///////////////////////////////////////////

        admin_user_notExists = PREFIX + format(properties.getProperty("slogin.admin.user.notExists"));
        admin_user_isNotOnline = PREFIX + format(properties.getProperty("slogin.admin.user.isNotOnline"));

        admin_commandList_title = format(properties.getProperty("slogin.admin.commandList.title"));
        admin_commandList_chatFormat = format(properties.getProperty("slogin.admin.commandList.chatFormat"));
        admin_commandList_hoverFormat = format(properties.getProperty("slogin.admin.commandList.hoverFormat"));

        admin_playerInfo_correctUsage = PREFIX + format(properties.getProperty("slogin.admin.playerInfo.correctUsage"));
        admin_playerInfo_message = format(properties.getProperty("slogin.admin.playerInfo.message"));

        admin_changePass_correctUsage = PREFIX + format(properties.getProperty("slogin.admin.changePass.correctUsage"));
        admin_changePass_success = PREFIX + format(properties.getProperty("slogin.admin.changePass.success"));

        admin_forceLogin_correctUsage = PREFIX + format(properties.getProperty("slogin.admin.forceLogin.correctUsage"));
        admin_forceLogin_success = PREFIX + format(properties.getProperty("slogin.admin.forceLogin.success"));
        admin_forceLogin_deny = PREFIX + format(properties.getProperty("slogin.admin.forceLogin.deny"));

        admin_register_correctUsage = PREFIX + format(properties.getProperty("slogin.admin.register.correctUsage"));
        admin_register_success = PREFIX + format(properties.getProperty("slogin.admin.register.success"));
        admin_register_deny = PREFIX + format(properties.getProperty("slogin.admin.register.deny"));

        admin_unRegister_correctUsage = PREFIX + format(properties.getProperty("slogin.admin.unRegister.correctUsage"));
        admin_unRegister_success = PREFIX + format(properties.getProperty("slogin.admin.unRegister.success"));
    }

    ///////////////////////////////////////////

    public final String misc_onlyForPlayers;
    public final String misc_nullValue;

    public final String system_alreadyLogged;
    public final String system_mustLogin;
    public final String system_maxAccounts;
    public final String system_wrongPassword_chatMessage;
    public final String system_wrongPassword_kickMessage;

    public final String system_notAllowedPasswordLength;
    public final String system_differentPasswords;
    public final String system_alreadyRegistered;
    public final String system_notRegistered;

    ///////////////////////////////////////////

    public final String auth_login_info;
    public final String auth_login_correctUsage;
    public final String auth_login_success;

    public final String auth_login_title;
    public final String auth_login_subTitle;

    public final String auth_login_successTitle;
    public final String auth_login_successSubTitle;

    public final String auth_login_timeoutKick;

    ///////////////////////////////////////////

    public final String auth_register_info;
    public final String auth_register_correctUsage;
    public final String auth_register_success;

    public final String auth_register_title;
    public final String auth_register_subTitle;

    public final String auth_register_successTitle;
    public final String auth_register_successSubTitle;

    ///////////////////////////////////////////

    public final String auth_changePass_correctUsage;
    public final String auth_changePass_success;

    ///////////////////////////////////////////

    public final String auth_email_info;
    public final String auth_email_correctUsage;
    public final String auth_email_changeSuccess;

    public final String auth_email_notSet;
    public final String auth_email_badFormat;

    ///////////////////////////////////////////

    public final String captcha_guiName;
    public final String captcha_kickMessage;

    ///////////////////////////////////////////

    public final String admin_user_notExists;
    public final String admin_user_isNotOnline;

    public final String admin_commandList_title;
    public final String admin_commandList_chatFormat;
    public final String admin_commandList_hoverFormat;

    public final String admin_playerInfo_correctUsage;
    public final String admin_playerInfo_message;

    public final String admin_changePass_correctUsage;
    public final String admin_changePass_success;

    public final String admin_forceLogin_correctUsage;
    public final String admin_forceLogin_success;
    public final String admin_forceLogin_deny;

    public final String admin_register_correctUsage;
    public final String admin_register_success;
    public final String admin_register_deny;

    public final String admin_unRegister_correctUsage;
    public final String admin_unRegister_success;

    ///////////////////////////////////////////

    public static String format(String s) {
        return (s == null) ? "<null>" : ChatColor.translateAlternateColorCodes('&', s);
    }

}
