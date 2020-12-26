package me.serafin.slogin.commands.admin;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.objects.Account;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UnRegisterCommand implements SubCommand {

    SLogin plugin;
    LangManager lang;

    public UnRegisterCommand(SLogin plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLangManager();
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

        Optional<Account> account = plugin.getLoginManager().getAccount(args[1]);
        if(!account.isPresent()){
            sender.sendMessage(lang.userNotExists);
            return;
        }

        plugin.getLoginManager().unRegister(args[1]);
        sender.sendMessage(lang.unRegisterSuccess);
    }
}
