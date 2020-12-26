package me.serafin.slogin.listeners;

import me.serafin.slogin.managers.LoginManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {

    LoginManager manager;
    public PlayerJoinListener(LoginManager manager){
        this.manager = manager;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event){
        manager.playerJoin(event.getPlayer());
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event){
        manager.playerQuit(event.getPlayer().getName());
    }

}
