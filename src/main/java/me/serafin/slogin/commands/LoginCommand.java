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

public class LoginCommand implements CommandExecutor {

    private final ConfigManager config;
    private final LangManager lang;
    private final LoginManager manager;

    public LoginCommand(){
        this.config = SLogin.getInstance().getConfigManager();
        this.lang = SLogin.getInstance().getLangManager();
        this.manager = SLogin.getInstance().getLoginManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage(lang.onlyForPlayers);
            return true;
        }
        Player player = (Player) sender;

        if(manager.isLogged(player.getName())){
            player.sendMessage(lang.alreadyLogged);
            return true;
        }

        if(!manager.isRegistered(player.getName())){
            player.sendMessage(lang.notRegistered);
            return true;
        }

        if(args.length != 1){
            player.sendMessage(lang.loginCorrectUsage);
            return true;
        }

        if(manager.login(player.getName(), Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress(), args[0], true)) {
            if(config.MESSAGES_TITLE_MESSAGES)
                player.sendTitle(lang.loginSuccessTitle, lang.loginSuccessSubTitle, 0, 4*10, 10);
            if(config.MESSAGES_CHAT_MESSAGES)
                player.sendMessage(lang.loginSuccess);
            manager.playerLogged(player);
        } else {
            player.sendMessage(lang.wrongPassword);
            if(config.KICK_ON_WRONG_PASSWORD)
                player.kickPlayer(lang.wrongPassword_kickMessage);
        }

        return true;
    }
}
