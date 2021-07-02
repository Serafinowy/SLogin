package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class LoginTimeoutManager {

    private final HashMap<Player, Integer> loginTimeout = new HashMap<>();
    private final ConfigManager configManager;

    public LoginTimeoutManager() {
        this.configManager = SLogin.getInstance().getConfigManager();

        if (configManager.getConfig().LOGIN_TIMEOUT > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Iterator<Map.Entry<Player, Integer>> iterator = loginTimeout.entrySet().iterator();

                    while (iterator.hasNext()) {
                        Map.Entry<Player, Integer> entry = iterator.next();

                        loginTimeout.put(entry.getKey(), entry.getValue() - 1);

                        if (entry.getValue() <= 0) {
                            iterator.remove();
                            Player player = entry.getKey();
                            player.kickPlayer(SLogin.getInstance().getLangManager().getLang(player.getLocale()).auth_login_timeoutKick);
                        }
                    }
                }
            }.runTaskTimer(SLogin.getInstance(), 0, 20);
        }
    }

    public void addTimeout(Player player) {
        if (configManager.getConfig().LOGIN_TIMEOUT > 0)
            loginTimeout.put(player, configManager.getConfig().LOGIN_TIMEOUT);
    }

    public void removeTimeout(Player player) {
        if (configManager.getConfig().LOGIN_TIMEOUT > 0)
            loginTimeout.remove(player);
    }

}
