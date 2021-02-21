package me.serafin.slogin.commands;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import me.serafin.slogin.objects.Account;
import me.serafin.slogin.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class EmailCommand implements CommandExecutor {

    private final LangManager lang;
    private final LoginManager manager;

    public EmailCommand() {
        this.lang = SLogin.getInstance().getLangManager();
        this.manager = SLogin.getInstance().getLoginManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(lang.onlyForPlayers);
            return true;
        }
        Player player = (Player) sender;

        Optional<Account> account = manager.getAccount(player.getName());
        assert account.isPresent();

        if(args.length == 0){
            if(account.get().getEmail() == null) {
                player.sendMessage(lang.emailNotSet);
            } else {
                player.sendMessage(lang.emailInfo.replace("{EMAIL}", account.get().getEmail()));
            }
        }

        else {
            if(args.length == 2 && args[0].equalsIgnoreCase("set")) {
                if(Utils.validateEmail(args[1])) {
                    manager.setEmail(account.get(), args[1]);
                    player.sendMessage(lang.emailChangeSuccess);
                }
                else player.sendMessage(lang.emailBadFormat);
            }
            else player.sendMessage(lang.emailCorrectUsage);
        }

        return true;
    }

}
