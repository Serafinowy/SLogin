package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.database.DataBase;
import me.serafin.slogin.objects.Account;
import me.serafin.slogin.objects.Lang;
import me.serafin.slogin.utils.BCrypt;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Optional;

public final class LoginManager {

    private final DataBase dataBase;
    private final LangManager langManager;
    private final ConfigManager config;

    public LoginManager(DataBase dataBase) {
        this.langManager = SLogin.getInstance().getLangManager();
        this.config = SLogin.getInstance().getConfigManager();
        this.dataBase = dataBase;
    }

    ///////////////////////////////////////

    /**
     * List of not logged in players
     */
    private final HashMap<String, Optional<Account>> tempAccounts = new HashMap<>();

    /**
     * Check if players is logged in
     *
     * @param name player's name
     * @return boolean - of player is logged in
     */
    public boolean isLogged(String name) {
        return !tempAccounts.containsKey(name);
    }

    /**
     * Check if player has account
     *
     * @param name player's name
     * @return player has account
     */
    public boolean isRegistered(String name) {
        return tempAccounts.get(name).isPresent();
    }

    ///////////////////////////////////////

    /**
     * Execute when player joined to the server
     *
     * @param player player
     */
    public void playerJoin(Player player) {
        SLogin.getInstance().getLoginTimeoutManager().addTimeout(player);

        Optional<Account> account = Account.get(dataBase, player.getName());
        tempAccounts.put(player.getName(), account);
        if (account.isPresent()) {
            if (config.CAPTCHA_ON_LOGIN)
                SLogin.getInstance().getCaptchaManager().sendCaptcha(player);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!isLogged(player.getName())) {
                        if (config.MESSAGES_CHAT_MESSAGES)
                            player.sendMessage(langManager.getLang(player.getLocale()).auth_login_info);
                        if (config.MESSAGES_TITLE_MESSAGES)
                            player.sendTitle(langManager.getLang(player.getLocale()).auth_login_title,
                                    langManager.getLang(player.getLocale()).auth_login_subTitle, 0, 4 * 20, 10);
                    }
                }
            }.runTaskLater(SLogin.getInstance(), 20);
        } else {
            if (config.CAPTCHA_ON_REGISTER)
                SLogin.getInstance().getCaptchaManager().sendCaptcha(player);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!isLogged(player.getName())) {
                        if (config.MESSAGES_CHAT_MESSAGES)
                            player.sendMessage(langManager.getLang(player.getLocale()).auth_register_info);
                        if (config.MESSAGES_TITLE_MESSAGES)
                            player.sendTitle(langManager.getLang(player.getLocale()).auth_register_title,
                                    langManager.getLang(player.getLocale()).auth_register_subTitle, 0, 4 * 20, 10);
                    }
                }
            }.runTaskLater(SLogin.getInstance(), 20);
        }
    }

    /**
     * Execute when player leaves from the server
     *
     * @param name player's name
     */
    public void playerQuit(String name) {
        tempAccounts.remove(name);
    }

    ///////////////////////////////////////

    /**
     * Login in player
     *
     * @param name            player's name
     * @param loginIP         player's login IP
     * @param password        player's password
     * @param requirePassword login even if password is incorrect
     * @return login success
     */
    public boolean login(String name, String loginIP, String password, boolean requirePassword) {
        if (!tempAccounts.containsKey(name))
            return false;

        Optional<Account> account = tempAccounts.get(name);
        if (account.isPresent()) {
            if (!requirePassword || account.get().comparePassword(password)) {
                account.get().update(Account.DataType.LAST_LOGIN_IP, loginIP);
                account.get().update(Account.DataType.LAST_LOGIN_DATE, String.valueOf(System.currentTimeMillis()));
                return true;
            }
        }
        return false;
    }

    /**
     * Register player
     *
     * @param name     player's name
     * @param password player's password
     * @param IP       player's register IP
     */
    public void register(String name, String password, String IP) {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        Account.create(dataBase, name, hashedPassword, IP);
    }

    /**
     * Execute when player successfully logged in
     *
     * @param player player
     */
    public void playerLogged(Player player, LoginType loginType) {
        player.setInvulnerable(false);
        SLogin.getInstance().getLoginTimeoutManager().removeTimeout(player);

        Lang lang = langManager.getLang(player.getLocale());

        if (loginType == LoginType.LOGIN) {
            if (config.MESSAGES_TITLE_MESSAGES)
                player.sendTitle(lang.auth_login_successTitle, lang.auth_login_successSubTitle, 0, 4 * 10, 10);
            if (config.MESSAGES_CHAT_MESSAGES)
                player.sendMessage(lang.auth_login_success);
        }
        if (loginType == LoginType.REGISTER) {
            if (config.MESSAGES_TITLE_MESSAGES)
                player.sendTitle(lang.auth_register_successTitle, lang.auth_register_successSubTitle, 0, 4 * 10, 10);
            if (config.MESSAGES_CHAT_MESSAGES) {
                player.sendMessage(lang.auth_register_success);
            }
        }

        Optional<Account> account = tempAccounts.get(player.getName());
        account = account.isPresent() ? account : Account.get(dataBase, player.getName());
        if (account.isPresent() && config.EMAIL_NOTIFICATION && account.get().getEmail() == null) {
            player.sendMessage(lang.auth_email_notSet);
        }
        tempAccounts.remove(player.getName());
    }

    public enum LoginType {
        LOGIN, REGISTER
    }

    ///////////////////////////////////////

    /**
     * Gets player's account
     */
    public Optional<Account> getAccount(String name) {
        return Account.get(dataBase, name);
    }

    /**
     * Get accounts number from one IP address
     *
     * @param address account address
     * @return account number
     */
    public int getAccountIPCount(String address) {
        return Account.accountIPCount(dataBase, address);
    }
}