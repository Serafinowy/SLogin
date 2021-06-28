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

    private final LangManager langManager;
    private final Set<String> tempCaptcha = new HashSet<>();

    public CaptchaManager() {
        this.langManager = SLogin.getInstance().getLangManager();
        SLogin.getInstance().getServer().getPluginManager().registerEvents(new Events(), SLogin.getInstance());
    }

    private Inventory inventory(String locale) {
        Random random = new Random();

        Material chose = Material.APPLE;
        int correctSlot = random.nextInt(27);

        Inventory inv = Bukkit.createInventory(null, 54, langManager.getLang(locale).captcha_guiName);

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, new ItemStack(Material.BARRIER));
        }

        inv.setItem(correctSlot, new ItemStack(chose));

        return inv;
    }

    public void sendCaptcha(Player player) {
        tempCaptcha.add(player.getName());
        new BukkitRunnable() {
            @Override
            public void run() {
                player.openInventory(inventory(player.getLocale()));
            }
        }.runTaskLater(SLogin.getInstance(), 20);
    }

    private class Events implements Listener {

        @EventHandler(priority = EventPriority.LOWEST)
        public void onClick(InventoryClickEvent event) {
            System.out.println(2);
            if (tempCaptcha.contains(event.getWhoClicked().getName())) {
                event.setCancelled(true);

                if (event.getCurrentItem() == null)
                    return;

                Player player = (Player) event.getWhoClicked();

                if (!event.getCurrentItem().getType().equals(Material.APPLE)) {
                    ((Player) event.getWhoClicked()).kickPlayer(langManager.getLang(player.getLocale()).captcha_kickMessage);
                }

                tempCaptcha.remove(event.getWhoClicked().getName());
                event.getWhoClicked().closeInventory();
            }
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onClose(InventoryCloseEvent event) {
            if (event.getView().getTitle().contains("Captcha") &&
                    tempCaptcha.contains(event.getPlayer().getName())) {
                Player player = (Player) event.getPlayer();
                player.kickPlayer(langManager.getLang(player.getLocale()).captcha_kickMessage);
            }
        }

    }
}
