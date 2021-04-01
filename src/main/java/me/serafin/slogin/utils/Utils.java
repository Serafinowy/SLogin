package me.serafin.slogin.utils;

import me.serafin.slogin.SLogin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utils {

    // Used to update config
    @SuppressWarnings("ConstantConditions")
    public static void matchConfig(FileConfiguration config, File file) {
        try {
            boolean hasUpdated = false;
            InputStream is = SLogin.getPlugin(SLogin.class).getResource(file.getName());
            assert is != null;
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(isr);
            for (String key : defConfig.getConfigurationSection("").getKeys(true)) {
                if (!config.contains(key)) {
                    config.set(key, defConfig.get(key));
                    hasUpdated = true;
                }
            }
            for (String key : config.getConfigurationSection("").getKeys(true)) {
                if (!defConfig.contains(key) && !key.equalsIgnoreCase("recipe-delay")) {
                    config.set(key, null);
                    hasUpdated = true;
                }
            }
            if (hasUpdated)
                config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TextComponent sendCommandSuggest(String text, String hover, String cmd) {
        TextComponent component = new TextComponent(text);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));
        return component;
    }

    ///////////////////////////////////////

    public static String getLatestVersion() {
        try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=87073").openStream();
             Scanner scanner = new Scanner(inputStream)) {
            if (scanner.hasNext()) {
                return scanner.next();
            }
        } catch (IOException exception) {
            Bukkit.getLogger().severe("Update checker is broken, cannot find an update!" + exception.getMessage());
        }
        return null;
    }

    public static String getServerVersion() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        String serverVersion = "1.8";

        if (bukkitVersion.contains("1.9")) serverVersion = "1.9";
        else if (bukkitVersion.contains("1.10")) serverVersion = "1.10";
        else if (bukkitVersion.contains("1.11")) serverVersion = "1.11";
        else if (bukkitVersion.contains("1.12")) serverVersion = "1.12";
        else if (bukkitVersion.contains("1.13")) serverVersion = "1.13";
        else if (bukkitVersion.contains("1.14")) serverVersion = "1.14";
        else if (bukkitVersion.contains("1.15")) serverVersion = "1.15";
        else if (bukkitVersion.contains("1.16")) serverVersion = "1.16";

        return serverVersion;
    }

    public static boolean isCorrectVersion(String curVersion, String minVersion) {
        String[] curVersionT = curVersion.split("\\.");
        String[] minVersionT = minVersion.split("\\.");

        try {
            if (Integer.parseInt(curVersionT[0]) > Integer.parseInt(minVersionT[0]))
                return true;
            return Integer.parseInt(curVersionT[1]) >= Integer.parseInt(minVersionT[1]);
        } catch (Exception ignored) {
        }
        return false;
    }

    ///////////////////////////////////////

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
