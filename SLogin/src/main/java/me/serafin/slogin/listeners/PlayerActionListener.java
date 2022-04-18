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

    private final ConfigManager configManager = SLogin.getInstance().getConfigManager();
    private final LangManager langManager = SLogin.getInstance().getLangManager();
    private final LoginManager loginManager = SLogin.getInstance().getLoginManager();

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        if (!loginManager.isLogged(event.getPlayer().getName())) {
            event.getPlayer().sendMessage(langManager.getLang(event.getPlayer().getLocale()).system_mustLogin);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!loginManager.isLogged(event.getWhoClicked().getName()) && !event.isCancelled()) {
            event.getWhoClicked().sendMessage(langManager.getLang(((Player) event.getWhoClicked()).getLocale()).system_mustLogin);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!loginManager.isLogged(event.getPlayer().getName())) {
            event.getPlayer().sendMessage(langManager.getLang(event.getPlayer().getLocale()).system_mustLogin);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onMove(PlayerMoveEvent event) {
        if (!loginManager.isLogged(event.getPlayer().getName())) {
            event.getPlayer().teleport(event.getFrom());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDrop(PlayerDropItemEvent event) {
        if (!loginManager.isLogged(event.getPlayer().getName())) {
            event.getPlayer().sendMessage(langManager.getLang(event.getPlayer().getLocale()).system_mustLogin);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage().split(" ")[0].replace("/", "");
        if (configManager.getConfig().ALLOWED_COMMANDS.contains(cmd))
            return;

        if (!loginManager.isLogged(event.getPlayer().getName())) {
            event.getPlayer().sendMessage(langManager.getLang(event.getPlayer().getLocale()).system_mustLogin);
            event.setCancelled(true);
        }
    }
}
