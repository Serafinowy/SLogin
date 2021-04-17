package me.serafin.slogin.commands.admin;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import me.serafin.slogin.objects.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public final class ForceLoginSubCommand implements SubCommand {

    private final LangManager langManager;
    private final LoginManager manager;

    public ForceLoginSubCommand() {
        this.langManager = SLogin.getInstance().getLangManager();
        this.manager = SLogin.getInstance().getLoginManager();
    }

    @Override
    public String getName() {
        return "forcelogin";
    }

    @Override
    public String getDescription() {
        return "Login specified player";
    }

    @Override
    public String getSyntax() {
        return "/sl forcelogin <nick>";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("forcelogin", "fl", "force", "f");
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {

        Lang lang = langManager.getLang("default");
        if (sender instanceof Player)
            lang = langManager.getLang(((Player) sender).getLocale());

        if (args.length != 2) {
            sender.sendMessage(lang.admin_forceLogin_correctUsage);
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(lang.admin_user_isNotOnline);
            return;
        }

        assert player.getAddress() != null;
        if (!manager.login(player.getName(), player.getAddress().getAddress().getHostAddress(), null, false)) {
            sender.sendMessage(lang.admin_forceLogin_deny);
            return;
        }

        manager.playerLogged(player, LoginManager.LoginType.LOGIN);
        sender.sendMessage(lang.admin_forceLogin_success);
    }
}
