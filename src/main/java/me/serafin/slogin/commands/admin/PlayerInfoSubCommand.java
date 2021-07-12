package me.serafin.slogin.commands.admin;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.AccountManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.models.Account;
import me.serafin.slogin.models.Lang;
import me.serafin.slogin.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class PlayerInfoSubCommand implements SubCommand {

    private final LangManager langManager;
    private final AccountManager accountManager;

    public PlayerInfoSubCommand() {
        this.langManager = SLogin.getInstance().getLangManager();
        this.accountManager = SLogin.getInstance().getAccountManager();
    }

    @Override
    public String getName() {
        return "playerinfo";
    }

    @Override
    public String getDescription(Lang lang) {
        return lang.admin_playerInfo_description;
    }

    @Override
    public String getSyntax() {
        return "/sl playerinfo <nick>";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("pinfo", "p");
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {

        Lang lang = langManager.getLang("default");
        if (sender instanceof Player)
            lang = langManager.getLang(((Player) sender).getLocale());

        if (args.length != 2) {
            sender.sendMessage(lang.admin_playerInfo_correctUsage);
            return;
        }

        Optional<Account> account = accountManager.getAccount(args[1]);
        if (!account.isPresent()) {
            sender.sendMessage(lang.admin_user_notExists);
            return;
        }

        sender.sendMessage(Utils.formatData(account.get(), lang.admin_playerInfo_message, lang));
    }
}
