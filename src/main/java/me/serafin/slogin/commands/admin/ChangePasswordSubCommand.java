package me.serafin.slogin.commands.admin;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import me.serafin.slogin.objects.Account;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class ChangePasswordSubCommand implements SubCommand {

    private final ConfigManager config;
    private final LangManager lang;
    private final LoginManager manager;

    public ChangePasswordSubCommand(){
        this.config = SLogin.getInstance().getConfigManager();
        this.lang = SLogin.getInstance().getLangManager();
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
        return Arrays.asList("changepassword", "cp", "changepass", "cpass", "change", "c");
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {
        if(args.length != 3){
            sender.sendMessage(lang.changePassAdminCorrectUsage);
            return;
        }

        Optional<Account> account = manager.getAccount(args[1]);
        if(!account.isPresent()){
            sender.sendMessage(lang.userNotExists);
            return;
        }

        if(args[2].length() < config.PASSWORD_MIN_LENGTH || args[2].length() > config.PASSWORD_MAX_LENGTH){
            sender.sendMessage(lang.notAllowedPasswordLength);
            return;
        }

        manager.setPassword(account.get(), args[2]);
        sender.sendMessage(lang.changePassAdminSuccess);
    }
}
