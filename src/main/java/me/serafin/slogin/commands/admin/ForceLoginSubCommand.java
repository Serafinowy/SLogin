package me.serafin.slogin.commands.admin;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ForceLoginSubCommand implements SubCommand {

    SLogin plugin;
    ConfigManager config;
    LangManager lang;
    LoginManager manager;

    public ForceLoginSubCommand(SLogin plugin){
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
        this.lang = plugin.getLangManager();
        this.manager = plugin.getLoginManager();
    }

    @Override
    public String getName() {
        return "forcelogin";
    }

    @Override
    public String getDescription() {
        return "Login specified player";
    }

    @Override
    public String getSyntax() {
        return "/sl forcelogin <nick>";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("forcelogin", "fl", "force", "f");
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {

        if(args.length != 2){
            sender.sendMessage(lang.forceLoginCorrectUsage);
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if(player == null){
            sender.sendMessage(lang.userIsNotOnline);
            return;
        }

        if(!manager.login(player.getName(), Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress(), null, true)){
            sender.sendMessage(lang.forceLoginDeny);
            return;
        }

        if(config.MESSAGES_TITLE_MESSAGES)
            player.sendTitle(lang.loginSuccessTitle, lang.loginSuccessSubTitle, 0, 4*10, 10);
        if(config.MESSAGES_CHAT_MESSAGES)
            player.sendMessage(lang.loginSuccess);

        player.setInvulnerable(false);
        sender.sendMessage(lang.forceLoginSuccess);

    }
}
