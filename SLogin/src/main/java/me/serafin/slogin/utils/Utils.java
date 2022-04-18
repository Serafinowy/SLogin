package me.serafin.slogin.utils;

import me.serafin.slogin.models.Account;
import me.serafin.slogin.models.Lang;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utils {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private Utils() {
    }

    /**
     * Check if given string is in email format.
     *
     * @param emailStr string to check
     * @return true if given string is in email format, false otherwise
     */
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    ///////////////////////////////////////

    /**
     * Get latest version of plugin from SpigotMC API.
     *
     * @return the String representing the version
     */
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

    /**
     * Checking if curVersion is greater of equals to minVersion.
     *
     * @param curVersion current version
     * @param minVersion minimum working version
     * @return true if curVersion is greater of equals to minVersion, false otherwise
     */
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

    /**
     * Creates a text component that displays the tooltip when hovered over and prepares the command when clicked.
     *
     * @param text  visible text in component
     * @param hover content of tooltip
     * @param cmd   command to be prepared
     * @return ready text component
     */
    @SuppressWarnings("deprecation")
    public static TextComponent sendCommandSuggest(String text, String hover, String cmd) {
        TextComponent component = new TextComponent(text);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(hover)}));
        return component;
    }

    ///////////////////////////////////////

    /**
     * Format placeholders in string.
     *
     * @param account player's account
     * @param pattern string to format
     * @return formatted string
     */
    public static String formatData(Account account, String pattern, Lang lang) {

        String datePattern = "dd.MM.yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        Date registerDate = new Date(account.getRegisterDate());
        Date lastLoginDate = new Date(account.getLastLoginDate());

        return pattern.replace("{PLAYER}", account.getDisplayName().toUpperCase())
                .replace("{EMAIL}", account.getEmail() == null ? lang.misc_nullValue : account.getEmail())
                .replace("{REGISTER_IP}", account.getRegisterIP())
                .replace("{REGISTER_DATE}", simpleDateFormat.format(registerDate))
                .replace("{LASTLOGIN_IP}", account.getLastLoginIP())
                .replace("{LASTLOGIN_DATE}", simpleDateFormat.format(lastLoginDate));
    }

}
