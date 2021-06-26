package me.serafin.slogin.utils;

import me.serafin.slogin.SLogin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class Utils {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    // Used to update config
    @SuppressWarnings("ConstantConditions")
    public static void matchConfig(FileConfiguration config, File file) {
        try (InputStream is = SLogin.getPlugin(SLogin.class).getResource(file.getName())) {
            boolean hasUpdated = false;
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

    ///////////////////////////////////////

    @SuppressWarnings("deprecation")
    public static TextComponent sendCommandSuggest(String text, String hover, String cmd) {
        TextComponent component = new TextComponent(text);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(hover)}));
        return component;
    }

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
        else if (bukkitVersion.contains("1.17")) serverVersion = "1.17";

        return serverVersion;
    }

    ///////////////////////////////////////

    public static boolean isCompatible(String curVersion, String minVersion) {
        String[] curVersionT = curVersion.split("\\.");
        String[] minVersionT = minVersion.split("\\.");

        try {
            // Check first number
            if (Integer.parseInt(curVersionT[0]) == Integer.parseInt(minVersionT[0])) {
                // Check second number
                if (Integer.parseInt(curVersionT[1]) == Integer.parseInt(minVersionT[1])) {
                    // Check third number
                    if (curVersionT.length == minVersionT.length) {
                        return curVersionT.length == 2 || Integer.parseInt(curVersionT[2]) >= Integer.parseInt(minVersionT[2]);
                    } else {
                        return curVersionT.length > minVersionT.length;
                    }
                } else {
                    return Integer.parseInt(curVersionT[1]) > Integer.parseInt(minVersionT[1]);
                }
            } else {
                return Integer.parseInt(curVersionT[0]) > Integer.parseInt(minVersionT[0]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static List<String> getAllResourcesIn(String path) {
        List<String> names = new ArrayList<>();

        try {
            CodeSource source = Utils.class.getProtectionDomain().getCodeSource();
            if (source != null) {
                URL jar = source.getLocation();
                ZipInputStream zis = new ZipInputStream(jar.openStream());
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    String name = entry.getName();
                    if (name.startsWith(path) && !name.equals(path + "/")) {
                        names.add(name);
                    }
                }
            } else {
                throw new IOException("Utils.class#getProtectionDomain()#getCodeSource() is null");
            }
            return names;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
