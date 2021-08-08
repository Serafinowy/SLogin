package me.serafin.slogin.commands;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.AccountManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.models.Account;
import me.serafin.slogin.models.Lang;
import me.serafin.slogin.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class EmailCommand implements CommandExecutor {

    private final LangManager langManager = SLogin.getInstance().getLangManager();
    private final AccountManager accountManager = SLogin.getInstance().getAccountManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(langManager.getLang("default").misc_onlyForPlayers);
            return true;
        }

        Player player = (Player) sender;
        Lang lang = langManager.getLang(player.getLocale());

        Optional<Account> account = accountManager.getAccount(player.getName());
        assert account.isPresent();

        if (args.length == 0) {
            if (account.get().getEmail() == null) {
                player.sendMessage(lang.auth_email_notSet);
            } else {
                player.sendMessage(lang.auth_email_info.replace("{EMAIL}", account.get().getEmail()));
            }
        } else {
            if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
                if (Utils.validateEmail(args[1])) {
                    accountManager.updateAccount(account.get(), AccountManager.DataType.EMAIL, args[1].toLowerCase());
                    player.sendMessage(lang.auth_email_changeSuccess);
                } else player.sendMessage(lang.auth_email_badFormat);
            } else player.sendMessage(lang.auth_email_correctUsage);
        }

        return true;
    }

}
