package me.serafin.slogin.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utils {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
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
        else if (bukkitVersion.contains("1.17")) serverVersion = "1.17";

        return serverVersion;
    }

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

    ///////////////////////////////////////
    @SuppressWarnings("deprecation")
    public static TextComponent sendCommandSuggest(String text, String hover, String cmd) {
        TextComponent component = new TextComponent(text);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(hover)}));
        return component;
    }

}
