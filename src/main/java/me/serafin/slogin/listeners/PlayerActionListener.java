package me.serafin.slogin.listeners;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerActionListener implements Listener {

    ConfigManager config;
    LangManager lang;
    LoginManager manager;
    public PlayerActionListener(SLogin plugin){
        this.config = SLogin.getInstance().configManager;
        this.lang = SLogin.getInstance().langManager;
        this.manager = SLogin.getInstance().loginManager;
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
        if(!manager.isLogged(event.getPlayer().getName())) {
            event.getPlayer().teleport(event.getFrom());
        }
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

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(!manager.isLogged(event.getPlayer().getName())){
            event.getPlayer().sendMessage(lang.mustLogin);
            event.setCancelled(true);
        }
    }
}
