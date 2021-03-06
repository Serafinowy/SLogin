package me.serafin.slogin.commands.admin;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import me.serafin.slogin.objects.Account;
import me.serafin.slogin.utils.Utils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public final class PlayerInfoSubCommand implements SubCommand {

    private final LangManager lang;
    private final LoginManager manager;

    public PlayerInfoSubCommand(){
        this.lang = SLogin.getInstance().getLangManager();
        this.manager = SLogin.getInstance().getLoginManager();
    }

    @Override
    public String getName() {
        return "playerinfo";
    }

    @Override
    public String getDescription() {
        return "Get player info";
    }

    @Override
    public String getSyntax(){
        return "/sl playerinfo <nick>";
    }

    @Override
    public List<String> getAliases(){
        return Arrays.asList("playerinfo", "pinfo", "p");
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {
        if(args.length != 2){
            sender.sendMessage(lang.playerInfoCorrectUsage);
            return;
        }

        Optional<Account> account = manager.getAccount(args[1]);
        if(!account.isPresent()){
            sender.sendMessage(lang.userNotExists);
            return;
        }

        sender.sendMessage(Account.formatData(account.get(), lang.playerInfoMessage));
    }
}
