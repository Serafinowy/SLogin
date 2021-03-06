package me.serafin.slogin.commands.admin;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import me.serafin.slogin.objects.Account;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class UnRegisterCommand implements SubCommand {

    private final LangManager lang;
    private final LoginManager manager;

    public UnRegisterCommand() {
        this.lang = SLogin.getInstance().getLangManager();
        this.manager = SLogin.getInstance().getLoginManager();
    }

    @Override
    public String getName() {
        return "unregister";
    }

    @Override
    public String getDescription() {
        return "Delete player's account";
    }

    @Override
    public String getSyntax() {
        return "/sl unregister <nick>";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("unregister", "unreg", "ur", "u");
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {
        if(args.length != 2) {
            sender.sendMessage(lang.unRegisterCorrectUsage);
            return;
        }

        Optional<Account> account = manager.getAccount(args[1]);
        if(!account.isPresent()){
            sender.sendMessage(lang.userNotExists);
            return;
        }

        manager.unRegister(account.get());
        sender.sendMessage(lang.unRegisterSuccess);
    }
}
