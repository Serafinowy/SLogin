package me.serafin.slogin.fastlogin;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import me.serafin.slogin.SLogin;
import org.bukkit.plugin.java.JavaPlugin;

public class FastLoginHook {

    private final SLoginHook sLoginHook;

    public FastLoginHook(SLogin plugin) {
        sLoginHook = new SLoginHook(plugin.getLogger(), plugin.getLoginManager());
        JavaPlugin.getPlugin(FastLoginBukkit.class).getCore().setAuthPluginHook(sLoginHook);
        plugin.getLogger().info("Hooked into FastLogin!");
    }

}
