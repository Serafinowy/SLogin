package me.serafin.slogin.managers;

import me.serafin.slogin.SLogin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

    private final LangManager lang;
    private final Set<String> tempCaptcha = new HashSet<>();

    public CaptchaManager() {
        this.lang = SLogin.getInstance().getLangManager();
        SLogin.getInstance().getServer().getPluginManager().registerEvents(new Events(), SLogin.getInstance());
    }

    private Inventory inventory() {
        Random random = new Random();

        Material chose = Material.APPLE;
        int correctSlot = random.nextInt(27);

        Inventory inv = Bukkit.createInventory(null, 54, lang.captcha_guiName);

        for(int i = 0; i < inv.getSize(); i++) {
            if(i == correctSlot)
                inv.setItem(i, new ItemStack(chose));
            else {
                inv.setItem(i, new ItemStack(Material.BARRIER));
            }
        }

        return inv;
    }

    public void sendCaptcha(Player player) {
        tempCaptcha.add(player.getName());
        new BukkitRunnable() {
            @Override
            public void run() {
                player.openInventory(inventory());
            }
        }.runTaskLater(SLogin.getInstance(), 10);
    }

    private class Events implements Listener {

        @EventHandler
        public void onClick(InventoryClickEvent event) {
            if(event.getView().getTitle().contains("Captcha")) {
                if(event.getCurrentItem() == null)
                    return;

                if(!event.getCurrentItem().getType().equals(Material.APPLE))
                    ((Player) event.getWhoClicked()).kickPlayer(lang.captcha_kickMessage);

                tempCaptcha.remove(event.getWhoClicked().getName());
                event.getWhoClicked().closeInventory();
                event.setCancelled(true);
            }
        }

        @EventHandler
        public void onClose(InventoryCloseEvent event){
            if(event.getView().getTitle().contains("Captcha") &&
                    tempCaptcha.contains(event.getPlayer().getName())) {
                ((Player) event.getPlayer()).kickPlayer(lang.captcha_kickMessage);
            }
        }

    }
}
