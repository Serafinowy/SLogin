package me.serafin.slogin.commands;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RegisterCommand implements CommandExecutor {

    ConfigManager config;
    LangManager lang;
    LoginManager manager;

    public RegisterCommand(SLogin plugin){
        this.config = plugin.getConfigManager();
        this.lang = plugin.getLangManager();
        this.manager = plugin.getLoginManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,@NotNull  Command command,@NotNull  String label, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage(lang.onlyForPlayers);
            return true;
        }
        Player player = (Player) sender;

        if(manager.isLogged(player.getName())){
            player.sendMessage(lang.alreadyLogged);
            return true;
        }

        if(manager.isRegistered(player.getName())){
            player.sendMessage(lang.alreadyRegistered);
            return true;
        }

        String address = Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress();
        if(!(manager.accountIPCount(address) < config.MAX_ACCOUNTS_PER_IP)){
            player.sendMessage(lang.maxAccounts);
            return true;
        }

        if(args.length != 2){
            player.sendMessage(lang.registerCorrectUsage);
            return true;
        }

        if(args[0].length() < config.PASSWORD_MIN_LENGTH || args[0].length() > config.PASSWORD_MAX_LENGTH){
            player.sendMessage(lang.notAllowedPasswordLength);
            return true;
        }

        if(!args[0].equals(args[1])){
            player.sendMessage(lang.differentPasswords);
            return true;
        }

        manager.register(player.getName(), args[0], player.getAddress().getAddress().getHostAddress());

        if(config.MESSAGES_TITLE_MESSAGES)
            player.sendTitle(lang.registerSuccessTitle, lang.registerSuccessSubTitle, 0, 4*10, 10);
        if(config.MESSAGES_CHAT_MESSAGES) {
            player.sendMessage(lang.registerSuccess);
        }
        player.setInvulnerable(false);

        return true;
    }
}