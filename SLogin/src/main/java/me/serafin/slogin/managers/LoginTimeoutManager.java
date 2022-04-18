package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.models.Lang;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class LoginTimeoutManager {

    private final SLogin plugin;
    private final ConfigManager configManager;
    private final HashMap<Player, Integer> loginTimeout = new HashMap<>();

    public LoginTimeoutManager(SLogin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();

        if (configManager.getConfig().LOGIN_TIMEOUT > 0) {
            runRunnable();
        }
    }

    private void runRunnable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Map.Entry<Player, Integer>> iterator = loginTimeout.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<Player, Integer> entry = iterator.next();

                    loginTimeout.put(entry.getKey(), entry.getValue() - 1);

                    Player player = entry.getKey();
                    Lang lang = plugin.getLangManager().getLang(player.getLocale());

                    if (entry.getValue() <= 0) {
                        iterator.remove();
                        player.kickPlayer(lang.auth_login_timeoutKick);
                    }

                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(String.format(lang.auth_login_timeoutInfo, entry.getValue())));
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    /**
     * Setting the player's timeout
     * @param player the player to be added to the database
     */
    public void addTimeout(Player player) {
        if (configManager.getConfig().LOGIN_TIMEOUT > 0)
            loginTimeout.put(player, configManager.getConfig().LOGIN_TIMEOUT);
    }

    /**
     * Removing the player's timeout
     * @param player the player to be removed from the database
     */
    public void removeTimeout(Player player) {
        if (configManager.getConfig().LOGIN_TIMEOUT > 0)
            loginTimeout.remove(player);
    }

}
