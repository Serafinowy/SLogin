package me.serafin.slogin.utils;

import me.serafin.slogin.SLogin;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class FileLoader {

    private FileLoader() {
    }

    /**
     * Checking if YAML file has all needed options. If not,
     * the entire file is overwritten with the default settings.
     *
     * @param fileToCheck file to be checked
     */
    @SuppressWarnings("ConstantConditions")
    public static void matchConfig(File fileToCheck) {
        FileConfiguration configToCheck = YamlConfiguration.loadConfiguration(fileToCheck);

        try (InputStream originalIS = SLogin.getInstance().getResource(fileToCheck.getName());
             InputStreamReader originalReader = new InputStreamReader(originalIS, StandardCharsets.UTF_8)
        ) {
            boolean hasUpdated = false;

            YamlConfiguration originalConfig = YamlConfiguration.loadConfiguration(originalReader);

            for (String key : originalConfig.getConfigurationSection("").getKeys(true)) {
                if (!configToCheck.contains(key)) {
                    configToCheck.set(key, originalConfig.get(key));
                    hasUpdated = true;
                }
            }
            for (String key : configToCheck.getConfigurationSection("").getKeys(true)) {
                if (!originalConfig.contains(key)) {
                    configToCheck.set(key, null);
                    hasUpdated = true;
                }
            }
            if (hasUpdated) {
                configToCheck.save(fileToCheck);
            }
        } catch (Exception e) {
            e.printStackTrace();
            SLogin.getInstance().getLogger().severe("Error while validating " + fileToCheck.getName() + " file");
        }
    }

    /**
     * Getting the string list of all files in specified path in jar
     *
     * @param path location to be searched
     * @return list of names of all files
     */
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
                throw new IOException("FileLoader source is null");
            }
            return names;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
