package me.serafin.slogin.listeners;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerActionListener implements Listener {

    ConfigManager config;
    LangManager lang;
    LoginManager manager;
    public PlayerActionListener(SLogin plugin){
        this.config = plugin.getConfigManager();
        this.lang = plugin.getLangManager();
        this.manager = plugin.getLoginManager();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(!manager.isLogged(event.getPlayer().getName())){
            event.getPlayer().sendMessage(lang.mustLogin);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        if(!manager.isLogged(event.getPlayer().getName())){
            event.getPlayer().sendMessage(lang.mustLogin);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(!manager.isLogged(event.getPlayer().getName()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        String cmd = event.getMessage().split(" ")[0].replace("/", "");
        if(config.ALLOWED_COMMANDS.contains(cmd))
            return;

        if(!manager.isLogged(event.getPlayer().getName())){
            event.getPlayer().sendMessage(lang.mustLogin);
            event.setCancelled(true);
        }
    }

}
