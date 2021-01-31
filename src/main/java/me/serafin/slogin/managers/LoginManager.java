package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.database.DataBase;
import me.serafin.slogin.objects.Account;
import me.serafin.slogin.utils.BCrypt;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Optional;

public class LoginManager {

    private final DataBase dataBase;
    private final LangManager lang;
    private final ConfigManager config;

    public LoginManager(DataBase dataBase) {
        this.lang = SLogin.getInstance().getLangManager();
        this.config = SLogin.getInstance().getConfigManager();
        this.dataBase = dataBase;
    }

    /**
     * List of not logged in players
     */
    private final HashMap<String, Optional<Account>> tempAccounts = new HashMap<>();

    /**
     * Check if players is logged in
     * @param name player's name
     * @return boolean - of player is logged in
     */
    public boolean isLogged(String name){
        return !tempAccounts.containsKey(name);
    }

    /**
     * Check if player has account
     * @param name player's name
     * @return player has account
     */
    public boolean isRegistered(String name){
        return tempAccounts.get(name).isPresent();
    }

    ///////////////////////////////////////

    /**
     * Execute when player joined to the server
     * @param player player
     */
    public void playerJoin(Player player){
        Optional<Account> account = Account.get(dataBase, player.getName());

        SLogin.getInstance().getLoginTimeoutManager().addTimeout(player);

        if(account.isPresent()) {
            tempAccounts.put(player.getName(), account);

            if(config.CAPTCHA_ON_LOGIN)
                SLogin.getInstance().getCaptchaManager().sendCaptcha(player);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(!isLogged(player.getName()))
                        if(config.MESSAGES_CHAT_MESSAGES)
                            player.sendMessage(lang.loginInfo);
                        if(config.MESSAGES_TITLE_MESSAGES)
                            player.sendTitle(lang.loginTitle, lang.loginSubTitle, 0, 4*20, 10);
                }
            }.runTaskLater(SLogin.getInstance(), 20);
        } else {
            tempAccounts.put(player.getName(), Optional.empty());

            if(config.CAPTCHA_ON_REGISTER)
                SLogin.getInstance().getCaptchaManager().sendCaptcha(player);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(!isLogged(player.getName()))
                        if(config.MESSAGES_CHAT_MESSAGES)
                            player.sendMessage(lang.registerInfo);
                        if(config.MESSAGES_TITLE_MESSAGES)
                            player.sendTitle(lang.registerTitle, lang.registerSubTitle, 0, 4*20, 10);
                }
            }.runTaskLater(SLogin.getInstance(), 20);
        }
    }

    /**
     * Execute when player leaves from the server
     * @param name player's name
     */
    public void playerQuit(String name) {
        tempAccounts.remove(name);
    }

    ///////////////////////////////////////

    /**
     * Login in player
     * @param name player's name
     * @param loginIP player's login IP
     * @param password player's password
     * @param bypass login even if password is incorrect
     * @return login success
     */
    public boolean login(String name, String loginIP, String password, boolean bypass) {
        if(!tempAccounts.containsKey(name))
            return false;

        Optional<Account> account = tempAccounts.get(name);
        if(account.isPresent()) {
            if(bypass || account.get().comparePassword(password)) {
                tempAccounts.remove(name);
                account.get().update(dataBase, account.get().getHashedPassword(), loginIP, System.currentTimeMillis());
                return true;
            }
        }
        return false;
    }

    /**
     * Register player
     * @param name player's name
     * @param password player's password
     * @param IP player's register IP
     */
    public void register(String name, String password, String IP) {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        Account.create(dataBase, name, hashedPassword, IP);
        tempAccounts.remove(name);
    }

    /**
     * Execute when player successfully logged in
     * @param player - player
     */
    public void playerLogged(Player player) {
        player.setInvulnerable(false);
        SLogin.getInstance().getLoginTimeoutManager().removeTimeout(player);
    }
    ///////////////////////////////////////

    /**
     * Password change
     * @param account player's account
     * @param password player's new password
     */
    public void changePassword(Account account, String password) {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        account.update(dataBase, hashedPassword, account.getLastLoginIP(), account.getLastLoginDate());
    }

    /**
     * Delete player's account
     * @param name player's name
     */
    public void unRegister(String name) {
        Optional<Account> account = Account.get(dataBase, name);
        account.ifPresent(value -> value.delete(dataBase));
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
     * @param address account address
     * @return account number
     */
    public int accountIPCount(String address) {
        return Account.accountIPCount(dataBase, address);
    }
}