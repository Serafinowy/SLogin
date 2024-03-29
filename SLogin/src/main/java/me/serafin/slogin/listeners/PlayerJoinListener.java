package me.serafin.slogin.listeners;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.LoginManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerJoinListener implements Listener {

    private final LoginManager loginManager;

    public PlayerJoinListener(SLogin plugin) {
        this.loginManager = plugin.getLoginManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        loginManager.playerJoin(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        loginManager.playerQuit(event.getPlayer().getName());
    }

}
