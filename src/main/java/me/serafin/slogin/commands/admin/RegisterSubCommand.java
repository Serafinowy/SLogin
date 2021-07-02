package me.serafin.slogin.commands.admin;

import me.serafin.slogin.SLogin;
import me.serafin.slogin.managers.ConfigManager;
import me.serafin.slogin.managers.LangManager;
import me.serafin.slogin.managers.LoginManager;
import me.serafin.slogin.models.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public final class RegisterSubCommand implements SubCommand {

    private final ConfigManager configManager;
    private final LangManager langManager;
    private final LoginManager loginManager;

    public RegisterSubCommand() {
        this.configManager = SLogin.getInstance().getConfigManager();
        this.langManager = SLogin.getInstance().getLangManager();
        this.loginManager = SLogin.getInstance().getLoginManager();
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getDescription(Lang lang) {
        return lang.admin_register_description;
    }

    @Override
    public String getSyntax() {
        return "/sl register <nick> <pass>";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("reg", "r");
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {

        Lang lang = langManager.getLang("default");
        if (sender instanceof Player)
            lang = langManager.getLang(((Player) sender).getLocale());

        if (args.length != 3) {
            sender.sendMessage(lang.admin_register_correctUsage);
            return;
        }

        if (loginManager.isRegistered(args[1])) {
            sender.sendMessage(lang.admin_register_deny);
            return;
        }

        if (args[2].length() < configManager.getConfig().PASSWORD_MIN_LENGTH || args[2].length() > configManager.getConfig().PASSWORD_MAX_LENGTH) {
            sender.sendMessage(lang.system_notAllowedPasswordLength);
            return;
        }

        loginManager.register(args[1], args[2], "adminCommand");
        sender.sendMessage(lang.admin_register_success);
    }
}
