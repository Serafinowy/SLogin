package me.serafin.slogin.fastlogin;

import com.github.games647.fastlogin.core.hooks.AuthPlugin;
import me.serafin.slogin.managers.LoginManager;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.logging.Logger;

public class SLoginHook implements AuthPlugin<Player> {

    private final Logger logger;
    private final LoginManager loginManager;

    public SLoginHook(Logger logger, LoginManager loginManager) {
        this.logger = logger;
        this.loginManager = loginManager;
    }

    @Override
    public boolean forceLogin(Player player) {
        boolean login = loginManager.login(player.getName(), Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress(), null, false);
        if (login) {
            loginManager.playerLogged(player, LoginManager.LoginType.LOGIN);
            logger.info("Logged " + player.getName() + " automatically!");
            return true;
        }
        logger.info("Error while logging in " + player.getName() + " automatically!");
        return false;
    }

    @Override
    public boolean forceRegister(Player player, String password) {
        loginManager.register(player.getName(), password, Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress());
        logger.info("Registered " + player.getName() + " automatically!");
        return true;
    }

    @Override
    public boolean isRegistered(String playerName) {
        return loginManager.isRegistered(playerName);
    }
}
