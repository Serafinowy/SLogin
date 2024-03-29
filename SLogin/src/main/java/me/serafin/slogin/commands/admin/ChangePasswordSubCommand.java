package me.serafin.slogin.commands.admin;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.AccountManager;
import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.models.Account;
import me.serafin.slogin.models.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class ChangePasswordSubCommand implements SubCommand {

    private final ConfigManager configManager;
    private final LangManager langManager;
    private final AccountManager accountManager;

    public ChangePasswordSubCommand(SLogin plugin) {
        this.configManager = plugin.getConfigManager();
        this.langManager = plugin.getLangManager();
        this.accountManager = plugin.getAccountManager();
    }

    @Override
    public String getName() {
        return "changepassword";
    }

    @Override
    public String getDescription(Lang lang) {
        return lang.admin_changePass_description;
    }

    @Override
    public String getSyntax() {
        return "/sl changepassword <nick> <pass>";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("cp", "changepass", "cpass", "change", "c");
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {

        Lang lang = langManager.getLang("default");
        if (sender instanceof Player)
            lang = langManager.getLang(((Player) sender).getLocale());

        if (args.length != 3) {
            sender.sendMessage(lang.admin_changePass_correctUsage);
            return;
        }

        Optional<Account> account = accountManager.getAccount(args[1]);
        if (!account.isPresent()) {
            sender.sendMessage(lang.admin_user_notExists);
            return;
        }

        if (args[2].length() < configManager.getConfig().PASSWORD_MIN_LENGTH || args[2].length() > configManager.getConfig().PASSWORD_MAX_LENGTH) {
            sender.sendMessage(lang.system_notAllowedPasswordLength);
            return;
        }

        accountManager.updateAccount(account.get(), AccountManager.DataType.PASSWORD, args[2]);
        sender.sendMessage(lang.admin_changePass_success);
    }
}
