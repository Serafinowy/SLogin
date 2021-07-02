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

public final class RegisterCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final LangManager langManager;
    private final LoginManager loginManager;

    public RegisterCommand() {
        this.configManager = SLogin.getInstance().getConfigManager();
        this.langManager = SLogin.getInstance().getLangManager();
        this.loginManager = SLogin.getInstance().getLoginManager();
    }

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

        if (loginManager.isRegistered(player.getName())) {
            player.sendMessage(lang.system_alreadyRegistered);
            return true;
        }

        String address = Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress();
        if (!loginManager.canRegister(address)) {
            player.sendMessage(lang.system_maxAccounts);
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(lang.auth_register_correctUsage);
            return true;
        }

        if (args[0].length() < configManager.getConfig().PASSWORD_MIN_LENGTH || args[0].length() > configManager.getConfig().PASSWORD_MAX_LENGTH) {
            player.sendMessage(lang.system_notAllowedPasswordLength);
            return true;
        }

        if (!args[0].equals(args[1])) {
            player.sendMessage(lang.system_differentPasswords);
            return true;
        }

        loginManager.register(player.getName(), args[0], player.getAddress().getAddress().getHostAddress());
        loginManager.playerLogged(player, LoginManager.LoginType.REGISTER);

        return true;
    }
}