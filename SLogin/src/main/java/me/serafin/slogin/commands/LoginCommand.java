package me.serafin.slogin.commands;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import me.serafin.slogin.models.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class LoginCommand implements CommandExecutor {

    private final ConfigManager configManager = SLogin.getInstance().getConfigManager();
    private final LangManager langManager = SLogin.getInstance().getLangManager();
    private final LoginManager loginManager = SLogin.getInstance().getLoginManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(langManager.getLang("default").misc_onlyForPlayers);
            return true;
        }

        Player player = (Player) sender;
        Lang lang = langManager.getLang(player.getLocale());

        if (loginManager.isLogged(player.getName())) {
            player.sendMessage(lang.system_alreadyLogged);
            return true;
        }

        if (!loginManager.isRegistered(player.getName())) {
            player.sendMessage(lang.system_notRegistered);
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(lang.auth_login_correctUsage);
            return true;
        }

        if (loginManager.login(player.getName(), Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress(), args[0], true)) {
            loginManager.playerLogged(player, LoginManager.LoginType.LOGIN);
        } else {
            player.sendMessage(lang.system_wrongPassword_chatMessage);
            if (configManager.getConfig().KICK_ON_WRONG_PASSWORD) {
                player.kickPlayer(lang.system_wrongPassword_kickMessage);
            }
        }

        return true;
    }
}
