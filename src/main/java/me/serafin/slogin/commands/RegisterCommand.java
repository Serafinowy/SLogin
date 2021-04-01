package me.serafin.slogin.commands;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import me.serafin.slogin.objects.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class RegisterCommand implements CommandExecutor {

    private final ConfigManager config;
    private final LangManager langManager;
    private final LoginManager manager;

    public RegisterCommand(){
        this.config = SLogin.getInstance().getConfigManager();
        this.langManager = SLogin.getInstance().getLangManager();
        this.manager = SLogin.getInstance().getLoginManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,@NotNull  Command command,@NotNull  String label, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage(langManager.getLang("default").misc_onlyForPlayers);
            return true;
        }
        Player player = (Player) sender;

        Lang lang = langManager.getLang(player.getLocale());

        if(manager.isLogged(player.getName())){
            player.sendMessage(lang.system_alreadyLogged);
            return true;
        }

        if(manager.isRegistered(player.getName())){
            player.sendMessage(lang.system_alreadyRegistered);
            return true;
        }

        String address = Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress();
        if(!(manager.getAccountIPCount(address) < config.MAX_ACCOUNTS_PER_IP)){
            player.sendMessage(lang.system_maxAccounts);
            return true;
        }

        if(args.length != 2){
            player.sendMessage(lang.auth_register_correctUsage);
            return true;
        }

        if(args[0].length() < config.PASSWORD_MIN_LENGTH || args[0].length() > config.PASSWORD_MAX_LENGTH){
            player.sendMessage(lang.system_notAllowedPasswordLength);
            return true;
        }

        if(!args[0].equals(args[1])){
            player.sendMessage(lang.system_differentPasswords);
            return true;
        }

        manager.register(player.getName(), args[0], player.getAddress().getAddress().getHostAddress());

        if(config.MESSAGES_TITLE_MESSAGES)
            player.sendTitle(lang.auth_register_successTitle, lang.auth_register_successSubTitle, 0, 4*10, 10);
        if(config.MESSAGES_CHAT_MESSAGES) {
            player.sendMessage(lang.auth_register_success);
        }
        manager.playerLogged(player);

        return true;
    }
}