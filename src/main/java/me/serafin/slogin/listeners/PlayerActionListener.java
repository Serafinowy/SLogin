package me.serafin.slogin.listeners;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public final class PlayerActionListener implements Listener {

    private final ConfigManager config;
    private final LangManager langManager;
    private final LoginManager manager;

    public PlayerActionListener(){
        this.config = SLogin.getInstance().getConfigManager();
        this.langManager = SLogin.getInstance().getLangManager();
        this.manager = SLogin.getInstance().getLoginManager();
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event){
        if(!manager.isLogged(event.getPlayer().getName())){
            event.getPlayer().sendMessage(langManager.getLang(event.getPlayer().getLocale()).system_mustLogin);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent event){
        if(!manager.isLogged(event.getWhoClicked().getName())){
            event.getWhoClicked().sendMessage(langManager.getLang(((Player) event.getWhoClicked()).getLocale()).system_mustLogin);
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event){
        if(!manager.isLogged(event.getPlayer().getName())){
            event.getPlayer().sendMessage(langManager.getLang(event.getPlayer().getLocale()).system_mustLogin);
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onMove(PlayerMoveEvent event){
        if(!manager.isLogged(event.getPlayer().getName())) {
            event.getPlayer().teleport(event.getFrom());
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event){
        String cmd = event.getMessage().split(" ")[0].replace("/", "");
        if(config.ALLOWED_COMMANDS.contains(cmd))
            return;

        if(!manager.isLogged(event.getPlayer().getName())){
            event.getPlayer().sendMessage(langManager.getLang(event.getPlayer().getLocale()).system_mustLogin);
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onDrop(PlayerDropItemEvent event) {
        if(!manager.isLogged(event.getPlayer().getName())){
            event.getPlayer().sendMessage(langManager.getLang(event.getPlayer().getLocale()).system_mustLogin);
            event.setCancelled(true);
        }
    }
}
