package me.serafin.slogin.commands.admin;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import me.serafin.slogin.objects.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class ForceLoginSubCommand implements SubCommand {

    private final ConfigManager config;
    private final LangManager langManager;
    private final LoginManager manager;

    public ForceLoginSubCommand() {
        this.config = SLogin.getInstance().getConfigManager();
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

        if (!manager.login(player.getName(), Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress(), null, false)) {
            sender.sendMessage(lang.admin_forceLogin_deny);
            return;
        }

        Lang playerLang = langManager.getLang(player.getLocale());

        if (config.MESSAGES_TITLE_MESSAGES)
            player.sendTitle(playerLang.auth_login_successTitle, playerLang.auth_login_successSubTitle, 0, 4 * 10, 10);
        if (config.MESSAGES_CHAT_MESSAGES)
            player.sendMessage(playerLang.auth_login_success);

        manager.playerLogged(player);
        sender.sendMessage(lang.admin_forceLogin_success);

    }
}
