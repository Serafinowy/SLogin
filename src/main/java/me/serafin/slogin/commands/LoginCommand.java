package me.serafin.slogin.commands;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import me.serafin.slogin.objects.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class LoginCommand implements CommandExecutor {

    private final ConfigManager config;
    private final LangManager langManager;
    private final LoginManager manager;

    public LoginCommand() {
        this.config = SLogin.getInstance().getConfigManager();
        this.langManager = SLogin.getInstance().getLangManager();
        this.manager = SLogin.getInstance().getLoginManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(langManager.getLang("default").misc_onlyForPlayers);
            return true;
        }
        Player player = (Player) sender;

        Lang lang = langManager.getLang(player.getLocale());

        if (manager.isLogged(player.getName())) {
            player.sendMessage(lang.system_alreadyLogged);
            return true;
        }

        if (!manager.isRegistered(player.getName())) {
            player.sendMessage(lang.system_notRegistered);
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(lang.auth_login_correctUsage);
            return true;
        }

        if (manager.login(player.getName(), Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress(), args[0], true)) {
            if (config.MESSAGES_TITLE_MESSAGES)
                player.sendTitle(lang.auth_login_successTitle, lang.auth_login_successSubTitle, 0, 4 * 10, 10);
            if (config.MESSAGES_CHAT_MESSAGES)
                player.sendMessage(lang.auth_login_success);
            manager.playerLogged(player);
        } else {
            player.sendMessage(lang.system_wrongPassword_chatMessage);
            if (config.KICK_ON_WRONG_PASSWORD)
                player.kickPlayer(lang.system_wrongPassword_kickMessage);
        }

        return true;
    }
}
