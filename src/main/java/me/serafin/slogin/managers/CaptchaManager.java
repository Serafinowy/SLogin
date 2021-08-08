package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public final class CaptchaManager {

    private final SLogin plugin = SLogin.getInstance();
    private final LangManager langManager = plugin.getLangManager();
    private final Set<String> tempCaptcha = new HashSet<>();

    private final Random random = new Random();

    public CaptchaManager() {
        plugin.getServer().getPluginManager().registerEvents(new Events(), plugin);
    }

    /**
     * Creates captcha inventory with messages in the player's locale
     * @param locale player's locale String
     * @return Inventory object
     */
    private Inventory createInventory(String locale) {
        int correctSlot = random.nextInt(54);
        Material chose = Material.APPLE;

        Inventory inventory = Bukkit.createInventory(null, 54, langManager.getLang(locale).captcha_guiName);

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemStack(Material.BARRIER));
        }

        inventory.setItem(correctSlot, new ItemStack(chose));

        return inventory;
    }

    /**
     * Adding the player to the captcha database and opening the captcha inventory.
     * @param player the player to be added to the database
     */
    public void sendCaptcha(Player player) {
        tempCaptcha.add(player.getName());
        new BukkitRunnable() {
            @Override
            public void run() {
                player.openInventory(createInventory(player.getLocale()));
            }
        }.runTaskLater(SLogin.getInstance(), 20);
    }

    private class Events implements Listener {

        @EventHandler(priority = EventPriority.LOWEST)
        public void onClick(InventoryClickEvent event) {
            if (tempCaptcha.contains(event.getWhoClicked().getName())) {
                event.setCancelled(true);

                if (event.getCurrentItem() == null)
                    return;

                Player player = (Player) event.getWhoClicked();

                if (!event.getCurrentItem().getType().equals(Material.APPLE)) {
                    player.kickPlayer(langManager.getLang(player.getLocale()).captcha_kickMessage);
                }

                tempCaptcha.remove(player.getName());
                player.closeInventory();
            }
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onClose(InventoryCloseEvent event) {
            if (tempCaptcha.contains(event.getPlayer().getName())) {
                Player player = (Player) event.getPlayer();
                player.kickPlayer(langManager.getLang(player.getLocale()).captcha_kickMessage);
            }
        }

    }
}
