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

public class RegisterSubCommand implements SubCommand {

    ConfigManager config;
    LangManager lang;
    LoginManager manager;

    public RegisterSubCommand(){
        this.config = SLogin.getInstance().configManager;
        this.lang = SLogin.getInstance().langManager;
        this.manager = SLogin.getInstance().loginManager;
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getDescription() {
        return "Register specified player";
    }

    @Override
    public String getSyntax() {
        return "/sl register <nick> <pass>";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("register", "reg", "r");
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {

        if(args.length != 3){
            sender.sendMessage(lang.registerAdminCorrectUsage);
            return;
        }

        Optional<Account> account = manager.getAccount(args[1]);
        if(account.isPresent()){
            sender.sendMessage(lang.registerAdminDeny);
            return;
        }

        if(args[2].length() < config.PASSWORD_MIN_LENGTH || args[2].length() > config.PASSWORD_MAX_LENGTH){
            sender.sendMessage(lang.notAllowedPasswordLength);
            return;
        }

        manager.register(args[1], args[2], "adminCommand");
        sender.sendMessage(lang.registerAdminSuccess);
    }
}
