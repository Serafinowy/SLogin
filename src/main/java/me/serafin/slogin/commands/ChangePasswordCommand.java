package me.serafin.slogin.commands;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import me.serafin.slogin.objects.Account;
import me.serafin.slogin.objects.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class ChangePasswordCommand implements CommandExecutor {

    private final ConfigManager config;
    private final LangManager langManager;
    private final LoginManager manager;

    public ChangePasswordCommand() {
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

        if (args.length != 2) {
            player.sendMessage(lang.auth_changePass_correctUsage);
            return true;
        }

        Optional<Account> account = manager.getAccount(player.getName());
        assert account.isPresent();

        if (!account.get().comparePassword(args[0])) {
            player.sendMessage(lang.system_wrongPassword_chatMessage);
            return true;
        }

        if (args[1].length() < config.PASSWORD_MIN_LENGTH || args[1].length() > config.PASSWORD_MAX_LENGTH) {
            player.sendMessage(lang.system_notAllowedPasswordLength);
            return true;
        }

        manager.setPassword(account.get(), args[1]);
        player.sendMessage(lang.auth_changePass_success);

        return true;
    }
}
