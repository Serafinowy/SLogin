package me.serafin.slogin.commands.admin;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import me.serafin.slogin.objects.Account;
import me.serafin.slogin.objects.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class ChangePasswordSubCommand implements SubCommand {

    private final ConfigManager config;
    private final LangManager langManager;
    private final LoginManager manager;

    public ChangePasswordSubCommand() {
        this.config = SLogin.getInstance().getConfigManager();
        this.langManager = SLogin.getInstance().getLangManager();
        this.manager = SLogin.getInstance().getLoginManager();
    }

    @Override
    public String getName() {
        return "changepassword";
    }

    @Override
    public String getDescription() {
        return "Change player's password";
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

        Optional<Account> account = manager.getAccount(args[1]);
        if (!account.isPresent()) {
            sender.sendMessage(lang.admin_user_notExists);
            return;
        }

        if (args[2].length() < config.PASSWORD_MIN_LENGTH || args[2].length() > config.PASSWORD_MAX_LENGTH) {
            sender.sendMessage(lang.system_notAllowedPasswordLength);
            return;
        }

        account.get().update(Account.DataType.PASSWORD, args[2]);
        sender.sendMessage(lang.admin_changePass_success);
    }
}
