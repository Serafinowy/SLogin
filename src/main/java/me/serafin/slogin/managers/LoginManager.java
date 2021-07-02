package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.models.Account;
import me.serafin.slogin.models.Config;
import me.serafin.slogin.models.Lang;
import me.serafin.slogin.utils.BCrypt;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Optional;

public final class LoginManager {

    private final ConfigManager configManager;
    private final LangManager langManager;
    private final AccountManager accountManager;

    /**
     * List of not logged in players
     */
    private final HashMap<String, Optional<Account>> notLoggedPlayers = new HashMap<>();

    ///////////////////////////////////////

    public LoginManager() {
        this.langManager = SLogin.getInstance().getLangManager();
        this.configManager = SLogin.getInstance().getConfigManager();
        this.accountManager = SLogin.getInstance().getAccountManager();
    }

    /**
     * Check if players is logged in
     *
     * @param name player's name
     * @return boolean - of player is logged in
     */
    public boolean isLogged(String name) {
        return !notLoggedPlayers.containsKey(name);
    }

    /**
     * Check if player has account
     *
     * @param name player's name
     * @return player has account
     */
    public boolean isRegistered(String name) {
        return accountManager.getAccount(name).isPresent();
    }

    ///////////////////////////////////////

    /**
     * Execute when player joined to the server
     *
     * @param player player
     */
    public void playerJoin(Player player) {
        player.setInvulnerable(true);
        SLogin.getInstance().getLoginTimeoutManager().addTimeout(player);

        Config config = configManager.getConfig();

        Optional<Account> account = accountManager.getAccount(player.getName());
        notLoggedPlayers.put(player.getName(), account);
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
            }.runTaskLater(SLogin.getInstance(), 2 * 20);
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
            }.runTaskLater(SLogin.getInstance(), 2 * 20);
        }
    }

    /**
     * Execute when player leaves from the server
     *
     * @param name player's name
     */
    public void playerQuit(String name) {
        notLoggedPlayers.remove(name);
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
        if (!notLoggedPlayers.containsKey(name))
            return false;

        Optional<Account> account = notLoggedPlayers.get(name);
        if (account.isPresent()) {
            if (!requirePassword || account.get().comparePassword(password)) {
                accountManager.updateAccount(account.get(), AccountManager.DataType.LAST_LOGIN_IP, loginIP);
                accountManager.updateAccount(account.get(), AccountManager.DataType.LAST_LOGIN_DATE, String.valueOf(System.currentTimeMillis()));
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
        accountManager.createAccount(name, hashedPassword, IP);
    }

    /**
     * Execute when player successfully logged in
     *
     * @param player player
     */
    public void playerLogged(Player player, LoginType loginType) {
        player.setInvulnerable(false);
        SLogin.getInstance().getLoginTimeoutManager().removeTimeout(player);

        Config config = configManager.getConfig();
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

        Optional<Account> account = notLoggedPlayers.get(player.getName());
        account = account.isPresent() ? account : accountManager.getAccount(player.getName());
        if (account.isPresent() && config.EMAIL_NOTIFICATION && account.get().getEmail() == null) {
            player.sendMessage(lang.auth_email_notSet);
        }
        notLoggedPlayers.remove(player.getName());
    }

    /**
     * Check if player can register new account. Returns true only if
     * player does not exceed the maximum number of accounts.
     * @param address - player's IP address
     * @return player can register new account
     */
    public boolean canRegister(String address) {
        int accountsCount = accountManager.accountIPCount(address);
        int maxAccountsPerIP = configManager.getConfig().MAX_ACCOUNTS_PER_IP;
        return accountsCount < maxAccountsPerIP || maxAccountsPerIP < 0;
    }

    ///////////////////////////////////////

    public enum LoginType {
        LOGIN, REGISTER
    }
}