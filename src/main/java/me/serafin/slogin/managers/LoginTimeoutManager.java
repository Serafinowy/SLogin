package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginTimeoutManager {

    private final HashMap<Player, Integer> loginTimeout = new HashMap<>();

    private final ConfigManager config;

    public LoginTimeoutManager(SLogin plugin) {
        this.config = plugin.getConfigManager();

        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Map.Entry<Player, Integer>> iterator = loginTimeout.entrySet().iterator();

                while(iterator.hasNext()) {
                    Map.Entry<Player, Integer> entry = iterator.next();

                    loginTimeout.put(entry.getKey(), entry.getValue() - 1);

                    if(entry.getValue() <= 0) {
                        removeTimeout(entry.getKey());
                        entry.getKey().kickPlayer(plugin.getLangManager().loginTimeoutKick);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void addTimeout(Player player) {
        loginTimeout.put(player, config.LOGIN_TIMEOUT);
    }

    public void removeTimeout(Player player) {
        loginTimeout.remove(player);
    }

}
