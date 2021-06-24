package me.serafin.slogin.commands.admin;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.objects.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public final class ReloadSubCommand implements SubCommand {

    private final SLogin plugin;
    private final LangManager langManager;

    public ReloadSubCommand() {
        plugin = SLogin.getInstance();
        langManager = plugin.getLangManager();
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads plugin settings";
    }

    @Override
    public String getSyntax() {
        return "/sl reload";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("rel");
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {

        Lang lang = langManager.getLang("default");
        if (sender instanceof Player)
            lang = langManager.getLang(((Player) sender).getLocale());

        if (plugin.setup())
            sender.sendMessage(lang.admin_reload_success);
        else
            sender.sendMessage(lang.admin_reload_error);

    }
}
